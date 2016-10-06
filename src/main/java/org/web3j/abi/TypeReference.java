package org.web3j.abi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * <p>Type wrapper to get around limitations of Java's type erasure.<br>
 * This is so that we can pass around Typed {@link org.web3j.abi.datatypes.Array} types.</p>
 *
 * <p>See <a href="http://gafter.blogspot.com.au/2006/12/super-type-tokens.html">this blog post</a>
 * for further details.</p>
 */
public abstract class TypeReference<T extends org.web3j.abi.datatypes.Type> implements Comparable<TypeReference<T>> {

    private final Type type;

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public int compareTo(TypeReference<T> o) {
        // taken from the blog post comments - this results in an errror if the
        // type parameter is left out.
        return 0;
    }

    public Type getType() {
        return type;
    }

    /**
     * Workaround to ensure type does not come back as T due to erasure, this enables you to
     * create a TypeReference via Class<T>.
     */
    @SuppressWarnings("unchecked")
    public Class<T> getClassType() throws ClassNotFoundException {
        Type clsType = getType();

        if (getType() instanceof ParameterizedTypeImpl) {
            return (Class<T>) ((ParameterizedTypeImpl) clsType).getRawType();
        } else {
            return (Class<T>) Class.forName(clsType.getTypeName());
        }
    }

    public static <T extends org.web3j.abi.datatypes.Type> TypeReference<T> create(Class<T> cls) {
        return new TypeReference<T>(){
            @Override
            public Type getType() {
                return cls;
            }
        };
    }

    public static abstract class StaticArrayTypeReference<T extends org.web3j.abi.datatypes.Type>
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
}
