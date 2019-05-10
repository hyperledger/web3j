package org.web3j.abi;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.BasicStruct;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Fixed;
import org.web3j.abi.datatypes.FixedPointType;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.IntType;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.StaticStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes1;
import org.web3j.abi.datatypes.generated.Bytes10;
import org.web3j.abi.datatypes.generated.Bytes11;
import org.web3j.abi.datatypes.generated.Bytes12;
import org.web3j.abi.datatypes.generated.Bytes13;
import org.web3j.abi.datatypes.generated.Bytes14;
import org.web3j.abi.datatypes.generated.Bytes15;
import org.web3j.abi.datatypes.generated.Bytes16;
import org.web3j.abi.datatypes.generated.Bytes17;
import org.web3j.abi.datatypes.generated.Bytes18;
import org.web3j.abi.datatypes.generated.Bytes19;
import org.web3j.abi.datatypes.generated.Bytes2;
import org.web3j.abi.datatypes.generated.Bytes20;
import org.web3j.abi.datatypes.generated.Bytes21;
import org.web3j.abi.datatypes.generated.Bytes22;
import org.web3j.abi.datatypes.generated.Bytes23;
import org.web3j.abi.datatypes.generated.Bytes24;
import org.web3j.abi.datatypes.generated.Bytes25;
import org.web3j.abi.datatypes.generated.Bytes26;
import org.web3j.abi.datatypes.generated.Bytes27;
import org.web3j.abi.datatypes.generated.Bytes28;
import org.web3j.abi.datatypes.generated.Bytes29;
import org.web3j.abi.datatypes.generated.Bytes3;
import org.web3j.abi.datatypes.generated.Bytes30;
import org.web3j.abi.datatypes.generated.Bytes31;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Bytes5;
import org.web3j.abi.datatypes.generated.Bytes6;
import org.web3j.abi.datatypes.generated.Bytes7;
import org.web3j.abi.datatypes.generated.Bytes8;
import org.web3j.abi.datatypes.generated.Bytes9;
import org.web3j.abi.datatypes.generated.Int104;
import org.web3j.abi.datatypes.generated.Int112;
import org.web3j.abi.datatypes.generated.Int120;
import org.web3j.abi.datatypes.generated.Int128;
import org.web3j.abi.datatypes.generated.Int136;
import org.web3j.abi.datatypes.generated.Int144;
import org.web3j.abi.datatypes.generated.Int152;
import org.web3j.abi.datatypes.generated.Int16;
import org.web3j.abi.datatypes.generated.Int160;
import org.web3j.abi.datatypes.generated.Int168;
import org.web3j.abi.datatypes.generated.Int176;
import org.web3j.abi.datatypes.generated.Int184;
import org.web3j.abi.datatypes.generated.Int192;
import org.web3j.abi.datatypes.generated.Int200;
import org.web3j.abi.datatypes.generated.Int208;
import org.web3j.abi.datatypes.generated.Int216;
import org.web3j.abi.datatypes.generated.Int224;
import org.web3j.abi.datatypes.generated.Int232;
import org.web3j.abi.datatypes.generated.Int24;
import org.web3j.abi.datatypes.generated.Int240;
import org.web3j.abi.datatypes.generated.Int248;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Int32;
import org.web3j.abi.datatypes.generated.Int40;
import org.web3j.abi.datatypes.generated.Int48;
import org.web3j.abi.datatypes.generated.Int56;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.Int72;
import org.web3j.abi.datatypes.generated.Int8;
import org.web3j.abi.datatypes.generated.Int80;
import org.web3j.abi.datatypes.generated.Int88;
import org.web3j.abi.datatypes.generated.Int96;
import org.web3j.abi.datatypes.generated.Uint104;
import org.web3j.abi.datatypes.generated.Uint112;
import org.web3j.abi.datatypes.generated.Uint120;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint136;
import org.web3j.abi.datatypes.generated.Uint144;
import org.web3j.abi.datatypes.generated.Uint152;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint160;
import org.web3j.abi.datatypes.generated.Uint168;
import org.web3j.abi.datatypes.generated.Uint176;
import org.web3j.abi.datatypes.generated.Uint184;
import org.web3j.abi.datatypes.generated.Uint192;
import org.web3j.abi.datatypes.generated.Uint200;
import org.web3j.abi.datatypes.generated.Uint208;
import org.web3j.abi.datatypes.generated.Uint216;
import org.web3j.abi.datatypes.generated.Uint224;
import org.web3j.abi.datatypes.generated.Uint232;
import org.web3j.abi.datatypes.generated.Uint24;
import org.web3j.abi.datatypes.generated.Uint240;
import org.web3j.abi.datatypes.generated.Uint248;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint40;
import org.web3j.abi.datatypes.generated.Uint48;
import org.web3j.abi.datatypes.generated.Uint56;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.abi.datatypes.generated.Uint72;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.abi.datatypes.generated.Uint80;
import org.web3j.abi.datatypes.generated.Uint88;
import org.web3j.abi.datatypes.generated.Uint96;
import org.web3j.utils.Numeric;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * <p>Ethereum Contract Application Binary Interface (ABI) decoding for types.
 * Decoding is not documented, but is the reverse of the encoding details located
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 * </p>
 */
public class TypeDecoder {

    static final int MAX_BYTE_LENGTH_FOR_HEX_STRING = Type.MAX_BYTE_LENGTH << 1;

    static <T extends Type> int getSingleElementLength(String input, int offset, Class<T> type) {
        if (input.length() == offset) {
            return 0;
        } else if (DynamicBytes.class.isAssignableFrom(type)
                || Utf8String.class.isAssignableFrom(type)) {
            // length field + data value
            return (decodeUintAsInt(input, offset) / Type.MAX_BYTE_LENGTH) + 2;
        } else {
            return 1;
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends Type> T decode(String input, int offset, Class<T> type) {
        if (NumericType.class.isAssignableFrom(type)) {
            return (T) decodeNumeric(input.substring(offset), (Class<NumericType>) type);
        } else if (Address.class.isAssignableFrom(type)) {
            return (T) decodeAddress(input.substring(offset));
        } else if (Bool.class.isAssignableFrom(type)) {
            return (T) decodeBool(input, offset);
        } else if (Bytes.class.isAssignableFrom(type)) {
            return (T) decodeBytes(input, offset, (Class<Bytes>) type);
        } else if (DynamicBytes.class.isAssignableFrom(type)) {
            return (T) decodeDynamicBytes(input, offset);
        } else if (Utf8String.class.isAssignableFrom(type)) {
            return (T) decodeUtf8String(input, offset);
        } else if (DynamicStructType.class.isAssignableFrom(type)) {
            return (T) decodeDynamicStructType(input, offset, 0, (Class<DynamicStructType>) type);
        } else if (StaticStructType.class.isAssignableFrom(type)) {
            return (T) decodeStaticStructType(input, offset, true, (Class<StaticStructType>) type);
        } else if (Array.class.isAssignableFrom(type)) {
            throw new UnsupportedOperationException(
                    "Array types must be wrapped in a TypeReference");
        } else {
            throw new UnsupportedOperationException(
                    "Type cannot be encoded: " + type.getClass());
        }
    }

    private static <T extends StaticStructType> T decodeStaticStructType(String input, int offset, boolean isTopLevelStruct, Class<StaticStructType> structType) {
        final List<TypeReference> structComponentTypes = getStructComponentTypes(structType);
        final Constructor<StaticStructType> structConstructor = findStructConstructor(structComponentTypes, structType);

        final int multiplier = 2;
        final int BYTES_TO_READ = multiplier * 32; // these are hex encoded as a string, so double the size :/

        // Treat this just like an array.
        int currentOffset = offset;

        // Parse each item according to the type.
        final List<Object> structConstructorArgs = new ArrayList<>();
        try {
            for (TypeReference structComponentType : structComponentTypes) {
                if (TypeReference.StaticArrayTypeReference.class.isAssignableFrom(structComponentType.getClass())) {
                    final int size = ((TypeReference.StaticArrayTypeReference) structComponentType).getSize();
                    final TypeReference.StaticArrayTypeReference<?> instance = determineArrayType(structComponentType.getType(), size);
                    structConstructorArgs.add(decodeStaticArray(input, currentOffset, instance, size));
                    currentOffset += instance.getSize() * BYTES_TO_READ;
                } else {
                    structConstructorArgs.add(decode(input, currentOffset, structComponentType.getClassType()));
                    currentOffset += BYTES_TO_READ;
                }
            }
            return (T) structConstructor.newInstance(structConstructorArgs.toArray());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("The struct (" + structType.getName() + ") does not implement: " + DynamicStructType.GET_TYPES_METHOD);
        }
    }

    private static TypeReference.StaticArrayTypeReference<?> determineArrayType(java.lang.reflect.Type arrayItemType, int size) {
        final int lastPackageIndex = arrayItemType.getTypeName().lastIndexOf(".");
        // This has to be a static type, which makes it easier!
        final String arrayItemTypeName = arrayItemType.getTypeName().toLowerCase().substring(lastPackageIndex + 1); // e.g. uint256

        // stolen from AbiType.java
        // TODO: is there a better way to do this?

        switch (arrayItemTypeName) {
            case "bool":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bool>>(size) {
                };
            case "uint8":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint8>>(size) {
                };
            case "int8":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int8>>(size) {
                };
            case "uint16":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint16>>(size) {
                };
            case "int16":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int16>>(size) {
                };
            case "uint24":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint24>>(size) {
                };
            case "int24":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int24>>(size) {
                };
            case "uint32":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint32>>(size) {
                };
            case "int32":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int32>>(size) {
                };
            case "uint40":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint40>>(size) {
                };
            case "int40":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int40>>(size) {
                };
            case "uint48":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint48>>(size) {
                };
            case "int48":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int48>>(size) {
                };
            case "uint56":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint56>>(size) {
                };
            case "int56":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int56>>(size) {
                };
            case "uint64":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint64>>(size) {
                };
            case "int64":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int64>>(size) {
                };
            case "uint72":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint72>>(size) {
                };
            case "int72":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int72>>(size) {
                };
            case "uint80":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint80>>(size) {
                };
            case "int80":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int80>>(size) {
                };
            case "uint88":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint88>>(size) {
                };
            case "int88":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int88>>(size) {
                };
            case "uint96":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint96>>(size) {
                };
            case "int96":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int96>>(size) {
                };
            case "uint104":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint104>>(size) {
                };
            case "int104":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int104>>(size) {
                };
            case "uint112":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint112>>(size) {
                };
            case "int112":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int112>>(size) {
                };
            case "uint120":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint120>>(size) {
                };
            case "int120":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int120>>(size) {
                };
            case "uint128":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint128>>(size) {
                };
            case "int128":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int128>>(size) {
                };
            case "uint136":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint136>>(size) {
                };
            case "int136":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int136>>(size) {
                };
            case "uint144":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint144>>(size) {
                };
            case "int144":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int144>>(size) {
                };
            case "uint152":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint152>>(size) {
                };
            case "int152":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int152>>(size) {
                };
            case "uint160":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint160>>(size) {
                };
            case "int160":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int160>>(size) {
                };
            case "uint168":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint168>>(size) {
                };
            case "int168":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int168>>(size) {
                };
            case "uint176":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint176>>(size) {
                };
            case "int176":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int176>>(size) {
                };
            case "uint184":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint184>>(size) {
                };
            case "int184":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int184>>(size) {
                };
            case "uint192":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint192>>(size) {
                };
            case "int192":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int192>>(size) {
                };
            case "uint200":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint200>>(size) {
                };
            case "int200":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int200>>(size) {
                };
            case "uint208":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint208>>(size) {
                };
            case "int208":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int208>>(size) {
                };
            case "uint216":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint216>>(size) {
                };
            case "int216":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int216>>(size) {
                };
            case "uint224":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint224>>(size) {
                };
            case "int224":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int224>>(size) {
                };
            case "uint232":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint232>>(size) {
                };
            case "int232":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int232>>(size) {
                };
            case "uint240":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint240>>(size) {
                };
            case "int240":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int240>>(size) {
                };
            case "uint248":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint248>>(size) {
                };
            case "int248":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int248>>(size) {
                };
            case "uint256":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Uint256>>(size) {
                };
            case "int256":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Int256>>(size) {
                };
            case "bytes1":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes1>>(size) {
                };
            case "bytes2":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes2>>(size) {
                };
            case "bytes3":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes3>>(size) {
                };
            case "bytes4":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes4>>(size) {
                };
            case "bytes5":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes5>>(size) {
                };
            case "bytes6":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes6>>(size) {
                };
            case "bytes7":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes7>>(size) {
                };
            case "bytes8":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes8>>(size) {
                };
            case "bytes9":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes9>>(size) {
                };
            case "bytes10":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes10>>(size) {
                };
            case "bytes11":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes11>>(size) {
                };
            case "bytes12":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes12>>(size) {
                };
            case "bytes13":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes13>>(size) {
                };
            case "bytes14":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes14>>(size) {
                };
            case "bytes15":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes15>>(size) {
                };
            case "bytes16":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes16>>(size) {
                };
            case "bytes17":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes17>>(size) {
                };
            case "bytes18":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes18>>(size) {
                };
            case "bytes19":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes19>>(size) {
                };
            case "bytes20":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes20>>(size) {
                };
            case "bytes21":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes21>>(size) {
                };
            case "bytes22":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes22>>(size) {
                };
            case "bytes23":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes23>>(size) {
                };
            case "bytes24":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes24>>(size) {
                };
            case "bytes25":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes25>>(size) {
                };
            case "bytes26":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes26>>(size) {
                };
            case "bytes27":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes27>>(size) {
                };
            case "bytes28":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes28>>(size) {
                };
            case "bytes29":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes29>>(size) {
                };
            case "bytes30":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes30>>(size) {
                };
            case "bytes31":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes31>>(size) {
                };
            case "bytes32":
                return new TypeReference.StaticArrayTypeReference<StaticArray<Bytes32>>(size) {
                };
            default:
                throw new IllegalArgumentException("Unsupported type: " + arrayItemTypeName);
        }
    }

    private static <T extends DynamicStructType> T decodeDynamicStructType(String input, int offset, int priorBlockOffset,
                                                                           Class<DynamicStructType> structType) {
        final int multiplier = 2;
        final int BYTES_TO_READ = multiplier * 32; // these are hex encoded as a string, so double the size :/

        final boolean isTopLevelStruct = priorBlockOffset == 0;
        final List<TypeReference> structComponentTypes = getStructComponentTypes(structType);
        final Constructor<DynamicStructType> structConstructor = findStructConstructor(structComponentTypes, structType);

        int currentOffset = offset;
        // 0x0...20
        final int structOffset = isTopLevelStruct ? Numeric.toBigInt(input.substring(currentOffset, BYTES_TO_READ)).intValue() : offset;
        currentOffset = structOffset * multiplier; // 0 + 32*2 = 64

        int currentBlockStartOffset = currentOffset; // 64

        // Parse each item according to the type.
        final List<Object> structConstructorArgs = new ArrayList<>();
        try {
            for (TypeReference structComponentType : structComponentTypes) {
                final boolean isDynamicComponentType = TypeEncoder.isDynamic(structComponentType);

                if (isDynamicComponentType) {
                    final int dynamicDataOffset = decodeUintAsInt(input, currentOffset);
                    final int actualDynamicDataIndex = dynamicDataOffset * multiplier + currentBlockStartOffset;

                    if (DynamicStructType.class.isAssignableFrom((Class) structComponentType.getType())) {
                        // Nested struct.
                        final Type oneComponent = decodeDynamicStructType(input, actualDynamicDataIndex / multiplier, currentBlockStartOffset, (Class) structComponentType.getType());
                        structConstructorArgs.add(oneComponent);
                        currentOffset += BYTES_TO_READ;
                    } else {
                        final Type oneComponent = decode(input, actualDynamicDataIndex, structComponentType.getClassType());
                        structConstructorArgs.add(oneComponent);
                        currentOffset += BYTES_TO_READ;
                    }

                } else {
                    final Type oneComponent = decode(input, currentOffset, structComponentType.getClassType());
                    structConstructorArgs.add(oneComponent);
                    currentOffset += BYTES_TO_READ;
                }
            }
            return (T) structConstructor.newInstance(structConstructorArgs.toArray());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("The struct (" + structType.getName() + ") does not implement: " + DynamicStructType.GET_TYPES_METHOD);
        }
    }

    /**
     * Helper function that finds the constructor of {@link DynamicStructType} instance that takes the given
     * argument types.
     *
     * @param structComponentTypes the struct component types.
     * @param structType           the struct typ.
     * @return the constructor to be invoked to create a new instance with the component types.
     */
    private static <STRUCT extends BasicStruct> Constructor<STRUCT> findStructConstructor(List<TypeReference> structComponentTypes, Class<STRUCT> structType) {
        final List<Class> types = new ArrayList<>(structComponentTypes.size());
        try {
            for (TypeReference structComponentType : structComponentTypes) {
                if (structComponentType instanceof TypeReference.StaticArrayTypeReference) {
                    types.add(StaticArray.class);
                } else {
                    types.add(structComponentType.getClassType()); // ((TypeReference.StaticArrayTypeReference) structComponentType).getSize()
                }

            }
            return structType.getConstructor(types.toArray(new Class[types.size()]));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot extract type from the struct component");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot find constructor with the given component types as arguments");
        }
    }

    /**
     * Inspects the given instance class definition of the struct and returns the (ordered) list of component types
     * in the struct.
     *
     * @param type the struct class type.
     * @return the list of types within the struct.
     */
    private static List<TypeReference> getStructComponentTypes(Class<? extends BasicStruct> type) {
        try {
            // There should be a static function named: getTypes().
            final Object typesObject = type.getMethod(DynamicStructType.GET_TYPES_METHOD).invoke(null);

            if (typesObject == null) {
                throw new IllegalArgumentException("The struct (" + type.getName() + ") " + DynamicStructType.GET_TYPES_METHOD +
                        " method returned null");
            }
            // we expect a list of type references.
            if (!(typesObject instanceof List)) {
                throw new IllegalArgumentException("Not a list");
            }

            return (List<TypeReference>) typesObject;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("The struct (" + type.getName() + ") does not implement: " + DynamicStructType.GET_TYPES_METHOD);
        }
    }

    public static <T extends Array> T decode(
            String input, int offset, TypeReference<T> typeReference) {
        Class cls = ((ParameterizedType) typeReference.getType()).getRawType().getClass();
        if (StaticArray.class.isAssignableFrom(cls)) {
            return decodeStaticArray(input, offset, typeReference, 1);
        } else if (DynamicArray.class.isAssignableFrom(cls)) {
            return decodeDynamicArray(input, offset, typeReference);
        } else {
            throw new UnsupportedOperationException("Unsupported TypeReference: "
                    + cls.getName() + ", only Array types can be passed as TypeReferences");
        }
    }

    static <T extends Type> T decode(String input, Class<T> type) {
        return decode(input, 0, type);
    }

    static Address decodeAddress(String input) {
        return new Address(decodeNumeric(input, Uint160.class));
    }

    static <T extends NumericType> T decodeNumeric(String input, Class<T> type) {
        try {
            byte[] inputByteArray = Numeric.hexStringToByteArray(input);
            int typeLengthAsBytes = getTypeLengthInBytes(type);

            byte[] resultByteArray = new byte[typeLengthAsBytes + 1];

            if (Int.class.isAssignableFrom(type) || Fixed.class.isAssignableFrom(type)) {
                resultByteArray[0] = inputByteArray[0];  // take MSB as sign bit
            }

            int valueOffset = Type.MAX_BYTE_LENGTH - typeLengthAsBytes;
            System.arraycopy(inputByteArray, valueOffset, resultByteArray, 1, typeLengthAsBytes);

            BigInteger numericValue = new BigInteger(resultByteArray);
            return type.getConstructor(BigInteger.class).newInstance(numericValue);

        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Unable to create instance of " + type.getName(), e);
        }
    }

    static <T extends NumericType> int getTypeLengthInBytes(Class<T> type) {
        return getTypeLength(type) >> 3;  // divide by 8
    }

    static <T extends NumericType> int getTypeLength(Class<T> type) {
        if (IntType.class.isAssignableFrom(type)) {
            String regex = "(" + Uint.class.getSimpleName() + "|" + Int.class.getSimpleName() + ")";
            String[] splitName = type.getSimpleName().split(regex);
            if (splitName.length == 2) {
                return Integer.parseInt(splitName[1]);
            }
        } else if (FixedPointType.class.isAssignableFrom(type)) {
            String regex = "(" + Ufixed.class.getSimpleName() + "|"
                    + Fixed.class.getSimpleName() + ")";
            String[] splitName = type.getSimpleName().split(regex);
            if (splitName.length == 2) {
                String[] bitsCounts = splitName[1].split("x");
                return Integer.parseInt(bitsCounts[0]) + Integer.parseInt(bitsCounts[1]);
            }
        }
        return Type.MAX_BIT_LENGTH;
    }

    static int decodeUintAsInt(String rawInput, int offset) {
        String input = rawInput.substring(offset, offset + MAX_BYTE_LENGTH_FOR_HEX_STRING);
        return decode(input, 0, Uint.class).getValue().intValue();
    }

    static Bool decodeBool(String rawInput, int offset) {
        String input = rawInput.substring(offset, offset + MAX_BYTE_LENGTH_FOR_HEX_STRING);
        BigInteger numericValue = Numeric.toBigInt(input);
        boolean value = numericValue.equals(BigInteger.ONE);
        return new Bool(value);
    }

    static <T extends Bytes> T decodeBytes(String input, Class<T> type) {
        return decodeBytes(input, 0, type);
    }

    static <T extends Bytes> T decodeBytes(String input, int offset, Class<T> type) {
        try {
            String simpleName = type.getSimpleName();
            String[] splitName = simpleName.split(Bytes.class.getSimpleName());
            int length = Integer.parseInt(splitName[1]);
            int hexStringLength = length << 1;

            byte[] bytes = Numeric.hexStringToByteArray(
                    input.substring(offset, offset + hexStringLength));
            return type.getConstructor(byte[].class).newInstance(bytes);
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Unable to create instance of " + type.getName(), e);
        }
    }

    static DynamicBytes decodeDynamicBytes(String input, int offset) {
        int encodedLength = decodeUintAsInt(input, offset);
        int hexStringEncodedLength = encodedLength << 1;

        int valueOffset = offset + MAX_BYTE_LENGTH_FOR_HEX_STRING;

        String data = input.substring(valueOffset,
                valueOffset + hexStringEncodedLength);
        byte[] bytes = Numeric.hexStringToByteArray(data);

        return new DynamicBytes(bytes);
    }

    static Utf8String decodeUtf8String(String input, int offset) {
        DynamicBytes dynamicBytesResult = decodeDynamicBytes(input, offset);
        byte[] bytes = dynamicBytesResult.getValue();

        return new Utf8String(new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * Static array length cannot be passed as a type.
     */
    @SuppressWarnings("unchecked")
    static <T extends Type> T decodeStaticArray(
            String input, int offset, TypeReference<T> typeReference, int length) {

        BiFunction<List<T>, String, T> function = (elements, typeName) -> {
            if (elements.isEmpty()) {
                throw new UnsupportedOperationException("Zero length fixed array is invalid type");
            } else {
                return instantiateStaticArray(typeReference, elements, length);
            }
        };

        return decodeArrayElements(input, offset, typeReference, length, function);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Type> T instantiateStaticArray(
            TypeReference<T> typeReference, List<T> elements, int length) {
        try {
            Class<? extends StaticArray> arrayClass =
                    (Class<? extends StaticArray>) Class.forName(
                            "org.web3j.abi.datatypes.generated.StaticArray" + length);

            return (T) arrayClass.getConstructor(List.class).newInstance(elements);
        } catch (ReflectiveOperationException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends Type> T decodeDynamicArray(
            String input, int offset, TypeReference<T> typeReference) {

        int length = decodeUintAsInt(input, offset);

        BiFunction<List<T>, String, T> function = (elements, typeName) -> {
            if (elements.isEmpty()) {
                return (T) DynamicArray.empty(typeName);
            } else {
                return (T) new DynamicArray<>(elements);
            }
        };

        int valueOffset = offset + MAX_BYTE_LENGTH_FOR_HEX_STRING;

        return decodeArrayElements(input, valueOffset, typeReference, length, function);
    }

    private static <T extends Type> T decodeArrayElements(
            String input, int offset, TypeReference<T> typeReference, int length,
            BiFunction<List<T>, String, T> consumer) {

        try {
            Class<T> cls = Utils.getParameterizedTypeFromArray(typeReference);
            if (Array.class.isAssignableFrom(cls)) {
                throw new UnsupportedOperationException(
                        "Arrays of arrays are not currently supported for external functions, see"
                                + "http://solidity.readthedocs.io/en/develop/types.html#members");
            } else {
                List<T> elements = new ArrayList<>(length);

                for (int i = 0, currOffset = offset;
                     i < length;
                     i++, currOffset += getSingleElementLength(input, currOffset, cls)
                             * MAX_BYTE_LENGTH_FOR_HEX_STRING) {
                    T value = decode(input, currOffset, cls);
                    elements.add(value);
                }

                String typeName = Utils.getSimpleTypeName(cls);

                return consumer.apply(elements, typeName);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException(
                    "Unable to access parameterized type " + typeReference.getType().getTypeName(),
                    e);
        }
    }
}
