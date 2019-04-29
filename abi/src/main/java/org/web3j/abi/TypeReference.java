/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.abi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.AbiTypes;


/**
 * Type wrapper to get around limitations of Java's type erasure. This is so that we can pass around
 * Typed {@link org.web3j.abi.datatypes.Array} types.
 *
 * <p>See <a href="http://gafter.blogspot.com.au/2006/12/super-type-tokens.html">this blog post</a>
 * for further details.
 *
 * <p>It may make sense to switch to using Java's reflection <a
 * href="https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Type.html">Type</a> to avoid
 * working around this fundamental generics limitation.
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
     * Workaround to ensure type does not come back as T due to erasure, this enables you to create
     * a TypeReference via {@link Class Class&lt;T&gt;}.
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

    public static <T extends org.web3j.abi.datatypes.Type> TypeReference<T> create(Class<T> cls,
                   boolean indexed) {
        return new TypeReference<T>(indexed) {
            public java.lang.reflect.Type getType() {
                return cls;
            }
        };
    }

    /*
     * This is a helper method that only works for atomic types (uint, bytes, etc).
     * Array types must be wrapped by a java.lang.reflect.ParamaterizedType
     */

    protected static Class getAtomicTypeClass(String solidityType) throws ClassNotFoundException {
        Matcher m = ARRAY_SUFFIX.matcher(solidityType);
        if (m.find()) {
            throw new ClassNotFoundException("getTypeClass does not work with array types."
                    + " See makeTypeRefernce()");
        }
        switch (solidityType) {
            case "int":
                return Int.class;
            case "uint":
                return Uint.class;
            default: return AbiTypes.getType(solidityType);
        }
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

    public static TypeReference makeTypeReference(String solidityType) throws
            ClassNotFoundException {
        return makeTypeReference(solidityType,false);
    }

    public static TypeReference makeTypeReference(String solidityType, final boolean indexed)
            throws ClassNotFoundException {
        Matcher m = ARRAY_SUFFIX.matcher(solidityType);
        if (!m.find()) {
            final Class tc = getAtomicTypeClass(solidityType);
            return create(tc, indexed);
        }
        String digits = m.group(1);
        TypeReference baseTr = makeTypeReference(solidityType.substring(0,solidityType.length()
                - m.group(0).length()));
        TypeReference<?> ref;
        if (digits == null || digits.equals("")) {
            ref = new TypeReference<DynamicArray>(indexed) {
                @Override
                public java.lang.reflect.Type getType() {
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
        } else {
            final Class arrayclass = Class.forName("org.web3j.abi.datatypes.generated.StaticArray"
                    + digits);
            ref = new TypeReference
                    .StaticArrayTypeReference<StaticArray>(Integer.parseInt(digits)) {
                @Override
                public boolean isIndexed() {
                    return indexed;
                }

                @Override
                public java.lang.reflect.Type getType() {
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
