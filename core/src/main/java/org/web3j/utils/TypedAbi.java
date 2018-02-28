package org.web3j.utils;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.AbiTypes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TypedAbi {
    public static class InvalidAbiType extends Exception {
        private String invalidTypeName;

        public InvalidAbiType(String name) {
            this.invalidTypeName = name;
        }

        @Override
        public String toString() {
            return "Invalid ABI type: " + invalidTypeName;
        }
    }

    public static class ArgRetType {
        // rawType is DynamicArray or StaticArray
        private Class<? extends Type> rawType;
        private Class<? extends Type> baseType;

        public ArgRetType(Class rawType, Class baseType) {
            this.rawType = rawType;
            this.baseType = baseType;
        }

        public ArgRetType(Class baseType) {
            this.rawType = null;
            this.baseType = baseType;
        }

        public static ArgRetType newDynamicArray(Class baseType) {
            return new ArgRetType(DynamicArray.class, baseType);
        }

        public static ArgRetType newStaticArray(String length, Class baseType) {
            Class staticArray;
            try {
                staticArray = Class.forName("org.web3j.abi.datatypes.generated.StaticArray" + length);
            } catch (ClassNotFoundException e) {
                staticArray = StaticArray.class;
            }
            return new ArgRetType(staticArray, baseType);
        }

        public Type javaToAbi(Object value) throws Exception {
            if (this.rawType != null) {
                // NOTE: only support Array and List
                Constructor arrayCtor = this.rawType.getDeclaredConstructor(List.class);
                List<Type> list = new ArrayList<>();
                if (value.getClass().isArray()) {
                    Object[] args = (Object[]) value;
                    for (Object arg : args) {
                        list.add(fromSingleJavaValue(arg));
                    }
                } else {
                    List<Object> args = (List<Object>)value;
                    for (Object arg: args) {
                        list.add(fromSingleJavaValue(arg));
                    }
                }
                return (Type)arrayCtor.newInstance(list);
            } else {
                return fromSingleJavaValue(value);
            }
        }

        private Type fromSingleJavaValue(Object value)
                throws NoSuchMethodException, InstantiationException, IllegalAccessException,
                InvocationTargetException, ClassNotFoundException, InvalidAbiType {
            String typeName = baseType.getSimpleName();
            Constructor ctor;
            if (typeName.equals("Address")) {
                ctor = Address.class.getDeclaredConstructor(String.class);
            } else if (typeName.equals("Bool")) {
                ctor = Bool.class.getDeclaredConstructor(boolean.class);
            } else if (typeName.equals("DynamicBytes")) {
                ctor = DynamicBytes.class.getDeclaredConstructor(byte[].class);
            } else if (typeName.equals("Utf8String")) {
                ctor = Utf8String.class.getDeclaredConstructor(String.class);
            } else if (typeName.startsWith("Uint") || typeName.startsWith("Int")) {
                Class c = Class.forName("org.web3j.abi.datatypes.generated." + typeName);
                ctor = c.getDeclaredConstructor(BigInteger.class);
            } else if (typeName.startsWith("Bytes")) {
                Class c = Class.forName("org.web3j.abi.datatypes.generated." + typeName);
                ctor = c.getDeclaredConstructor(byte[].class);
            } else {
                throw new InvalidAbiType(typeName);
            }
            return (Type)ctor.newInstance(value);
        }

        public Object abiToJava(Type value) {
            if (this.rawType != null) {
                List<Object> result = new ArrayList();
                List<Type> list = (List)value.getValue();
                list.forEach(type -> result.add(type.getValue()));
                return result;
            } else {
                return value.getValue();
            }
        }

        // decode failed when pass DynamicArray.class or StaticArray.class because of type erasure
        // we need to construct ParameterizedType manually
        public TypeReference getTypeReference() {
            if (this.rawType != null) {
                ParameterizedType parameterizedType = new ParameterizedType() {
                    @Override
                    public java.lang.reflect.Type[] getActualTypeArguments() {
                        return new java.lang.reflect.Type[]{ArgRetType.this.baseType};
                    }

                    @Override
                    public java.lang.reflect.Type getRawType() {
                        return ArgRetType.this.rawType;
                    }

                    @Override
                    public java.lang.reflect.Type getOwnerType() {
                        return null;
                    }
                };
                return TypeReference.create(parameterizedType);
            } else {
                return TypeReference.create(this.baseType);
            }
        }
    }

    public static ArgRetType getArgRetType(String typeName) {
        String type = trimStorageDeclaration(typeName);

        if (type.endsWith("]")) {
            String[] splitType = type.split("[\\[\\]]");
            Class<?> baseType = AbiTypes.getType(splitType[0]);

            if (splitType.length == 1) {
                return ArgRetType.newDynamicArray(baseType);
            } else {
                return ArgRetType.newStaticArray(splitType[1], baseType);
            }
        } else {
            return new ArgRetType(AbiTypes.getType(type));
        }
    }

    public static Type getType(String typeName, Object value) throws Exception {
        ArgRetType t = getArgRetType(typeName);
        return t.javaToAbi(value);
    }

    public static String trimStorageDeclaration(String type) {
        if (type.endsWith(" storage") || type.endsWith(" memory")) {
            return type.split(" ")[0];
        } else {
            return type;
        }
    }
}
