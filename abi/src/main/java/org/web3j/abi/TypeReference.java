package org.web3j.abi;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.AbiTypes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Type wrapper to get around limitations of Java's type erasure.
 * This is so that we can pass around Typed {@link org.web3j.abi.datatypes.Array} types.
 *
 * <p>See <a href="http://gafter.blogspot.com.au/2006/12/super-type-tokens.html">this blog post</a>
 * for further details.
 *
 * <p>It may make sense to switch to using Java's reflection
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Type.html">Type</a> to
 * avoid working around this fundamental generics limitation.
 */
public abstract class TypeReference<T extends org.web3j.abi.datatypes.Type>
        implements Comparable<TypeReference<T>> {
    protected static Pattern ARRAY_SUFFIX = Pattern.compile("\\[(\\d*)\\]$");

    private final Type type;
    private final boolean indexed;

    protected TypeReference() {
        this(false);
    }

    protected TypeReference(boolean indexed) {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        this.indexed = indexed;
    }

    public int compareTo(TypeReference<T> o) {
        // taken from the blog post comments - this results in an errror if the
        // type parameter is left out.
        return 0;
    }

    public Type getType() {
        return type;
    }

    public boolean isIndexed() {
        return indexed;
    }

    /**
     * Workaround to ensure type does not come back as T due to erasure, this enables you to
     * create a TypeReference via {@link Class Class&lt;T&gt;}.
     *
     * @return the parameterized Class type if applicable, otherwise a regular class
     * @throws ClassNotFoundException if the class type cannot be determined
     */
    @SuppressWarnings("unchecked")
    public Class<T> getClassType() throws ClassNotFoundException {
        Type clsType = getType();

        if (getType() instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) clsType).getRawType();
        } else {
            return (Class<T>) Class.forName(clsType.getTypeName());
        }
    }

    public static <T extends org.web3j.abi.datatypes.Type> TypeReference<T> create(Class<T> cls) {
        return create(cls, false);
    }
    public static <T extends org.web3j.abi.datatypes.Type> TypeReference<T> create(Class<T> cls, boolean indexed) {
        return new TypeReference<T>(indexed) {
            public java.lang.reflect.Type getType() {
                return cls;
            }
        };
    }
    /**
     * This is a helper method that only works for atomic types (uint, bytes, etc). Array types must be wrapped by a java.lang.reflect.ParamaterizedType
     * @param solidity_type
     * @return
     * @throws ClassNotFoundException
     */

    protected static Class getAtomicTypeClass(String solidity_type) throws ClassNotFoundException {
        Matcher m = ARRAY_SUFFIX.matcher(solidity_type);
        if (m.find()) {
            throw new ClassNotFoundException("getTypeClass does not work with array types. See makeTypeRefernce()");
        }
        switch (solidity_type) {
            case "int":
                return Int.class;
            case "uint":
                return Uint.class;
        }
        Class c = AbiTypes.getType(solidity_type);
        return c;
    }

    public abstract static class StaticArrayTypeReference<T extends org.web3j.abi.datatypes.Type>
            extends TypeReference<T> {

        private final int size;

        protected StaticArrayTypeReference(int size) {
            super();
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
    public static TypeReference makeTypeReference(String solidity_type) throws ClassNotFoundException {
        return makeTypeReference(solidity_type,false);
    }
    public static TypeReference makeTypeReference(String solidity_type, final boolean indexed) throws ClassNotFoundException {
        Matcher m = ARRAY_SUFFIX.matcher(solidity_type);
        if(!m.find()) {
            final Class tc = getAtomicTypeClass(solidity_type);
            return create(tc, indexed);
        }
        String digits = m.group(1);
        TypeReference baseTr = makeTypeReference(solidity_type.substring(0,solidity_type.length() - m.group(0).length()));
        TypeReference<?> ref;
        if (digits == null || digits.equals("")) {
            ref = new TypeReference<DynamicArray>(indexed) {
                @Override
                public java.lang.reflect.Type getType(){
                    return new ParameterizedType() {
                        @Override
                        public java.lang.reflect.Type[] getActualTypeArguments() {
                            return new java.lang.reflect.Type[]{baseTr.getType()};
                        }

                        @Override
                        public java.lang.reflect.Type getRawType() {
                            return DynamicArray.class;
                        }

                        @Override
                        public java.lang.reflect.Type getOwnerType() {
                            return Class.class;
                        }
                    };
                }
            };
        }
        else {
            final Class arrayclass = Class.forName("org.web3j.abi.datatypes.generated.StaticArray" + digits);
            ref = new TypeReference.StaticArrayTypeReference<StaticArray>(Integer.parseInt(digits)){
                @Override
                public boolean isIndexed(){
                    return indexed;
                }
                @Override
                public java.lang.reflect.Type getType(){
                    return new ParameterizedType() {
                        @Override
                        public java.lang.reflect.Type[] getActualTypeArguments() {
                            return new java.lang.reflect.Type[]{baseTr.getType()};
                        }
                        @Override
                        public java.lang.reflect.Type getRawType() {
                           return arrayclass;
                        }
                        @Override
                        public java.lang.reflect.Type getOwnerType() {
                            return Class.class;
                        }
                    };
                }
            };
        }
        return ref;
    }
}
