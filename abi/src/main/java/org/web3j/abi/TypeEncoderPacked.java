package org.web3j.abi;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.primitive.PrimitiveType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import static org.web3j.abi.datatypes.Type.MAX_BIT_LENGTH;
import static org.web3j.abi.datatypes.Type.MAX_BYTE_LENGTH;

public class TypeEncoderPacked {

    @SuppressWarnings("unchecked")
    public static String encode(Type parameter) {
        if (parameter instanceof NumericType) {
            return encodeNumeric(((NumericType) parameter));
//        } else if (parameter instanceof Address) {
//            return encodeAddress((Address) parameter);
//        } else if (parameter instanceof Bool) {
//            return encodeBool((Bool) parameter);
//        } else if (parameter instanceof Bytes) {
//            return encodeBytes((Bytes) parameter);
//        } else if (parameter instanceof DynamicBytes) {
//            return encodeDynamicBytes((DynamicBytes) parameter);
//        } else if (parameter instanceof Utf8String) {
//            return encodeString((Utf8String) parameter);
//        } else if (parameter instanceof StaticArray) {
//            if (DynamicStruct.class.isAssignableFrom(
//                    ((StaticArray) parameter).getComponentType())) {
//                return encodeStaticArrayWithDynamicStruct((StaticArray) parameter);
//            } else {
//                return encodeArrayValues((StaticArray) parameter);
//            }
//        } else if (parameter instanceof DynamicStruct) {
//            return encodeDynamicStruct((DynamicStruct) parameter);
//        } else if (parameter instanceof DynamicArray) {
//            return encodeDynamicArray((DynamicArray) parameter);
//        } else if (parameter instanceof PrimitiveType) {
//            return encode(((PrimitiveType) parameter).toSolidityType());
        } else {
            throw new UnsupportedOperationException(
                    "Type cannot be encoded: " + parameter.getClass());
        }
    }


    static String encodeNumeric(NumericType numericType) {
        byte[] value = toByteArray(numericType);
        return Numeric.toHexStringNoPrefix(value);
    }

    private static byte[] toByteArray(NumericType numericType) {
        BigInteger value = numericType.getValue();
        if (numericType instanceof Ufixed || numericType instanceof Uint) {
                byte[] byteArray = new byte[numericType.getBitSize()];
                System.arraycopy(value.toByteArray(), 0, byteArray, 0, numericType.getBitSize());
                return byteArray;
            }
        return value.toByteArray();
    }

//    private static byte[] asByteArray(BigInteger value){
//        return value.toByteArray().;
//    }
}
