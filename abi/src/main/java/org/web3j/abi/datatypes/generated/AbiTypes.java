package org.web3j.abi.datatypes.generated;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesMapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public final class AbiTypes {
    private AbiTypes() {
    }

    public static Class<? extends Type> getType(String type) {
        switch (type) {
            case "address":
                return Address.class;
            case "bool":
                return Bool.class;
            case "string":
                return Utf8String.class;
            case "bytes":
                return DynamicBytes.class;
            case "uint8":
                return Uint8.class;
            case "int8":
                return Int8.class;
            case "uint16":
                return Uint16.class;
            case "int16":
                return Int16.class;
            case "uint24":
                return Uint24.class;
            case "int24":
                return Int24.class;
            case "uint32":
                return Uint32.class;
            case "int32":
                return Int32.class;
            case "uint40":
                return Uint40.class;
            case "int40":
                return Int40.class;
            case "uint48":
                return Uint48.class;
            case "int48":
                return Int48.class;
            case "uint56":
                return Uint56.class;
            case "int56":
                return Int56.class;
            case "uint64":
                return Uint64.class;
            case "int64":
                return Int64.class;
            case "uint72":
                return Uint72.class;
            case "int72":
                return Int72.class;
            case "uint80":
                return Uint80.class;
            case "int80":
                return Int80.class;
            case "uint88":
                return Uint88.class;
            case "int88":
                return Int88.class;
            case "uint96":
                return Uint96.class;
            case "int96":
                return Int96.class;
            case "uint104":
                return Uint104.class;
            case "int104":
                return Int104.class;
            case "uint112":
                return Uint112.class;
            case "int112":
                return Int112.class;
            case "uint120":
                return Uint120.class;
            case "int120":
                return Int120.class;
            case "uint128":
                return Uint128.class;
            case "int128":
                return Int128.class;
            case "uint136":
                return Uint136.class;
            case "int136":
                return Int136.class;
            case "uint144":
                return Uint144.class;
            case "int144":
                return Int144.class;
            case "uint152":
                return Uint152.class;
            case "int152":
                return Int152.class;
            case "uint160":
                return Uint160.class;
            case "int160":
                return Int160.class;
            case "uint168":
                return Uint168.class;
            case "int168":
                return Int168.class;
            case "uint176":
                return Uint176.class;
            case "int176":
                return Int176.class;
            case "uint184":
                return Uint184.class;
            case "int184":
                return Int184.class;
            case "uint192":
                return Uint192.class;
            case "int192":
                return Int192.class;
            case "uint200":
                return Uint200.class;
            case "int200":
                return Int200.class;
            case "uint208":
                return Uint208.class;
            case "int208":
                return Int208.class;
            case "uint216":
                return Uint216.class;
            case "int216":
                return Int216.class;
            case "uint224":
                return Uint224.class;
            case "int224":
                return Int224.class;
            case "uint232":
                return Uint232.class;
            case "int232":
                return Int232.class;
            case "uint240":
                return Uint240.class;
            case "int240":
                return Int240.class;
            case "uint248":
                return Uint248.class;
            case "int248":
                return Int248.class;
            case "uint256":
                return Uint256.class;
            case "int256":
                return Int256.class;
            case "bytes1":
                return Bytes1.class;
            case "bytes2":
                return Bytes2.class;
            case "bytes3":
                return Bytes3.class;
            case "bytes4":
                return Bytes4.class;
            case "bytes5":
                return Bytes5.class;
            case "bytes6":
                return Bytes6.class;
            case "bytes7":
                return Bytes7.class;
            case "bytes8":
                return Bytes8.class;
            case "bytes9":
                return Bytes9.class;
            case "bytes10":
                return Bytes10.class;
            case "bytes11":
                return Bytes11.class;
            case "bytes12":
                return Bytes12.class;
            case "bytes13":
                return Bytes13.class;
            case "bytes14":
                return Bytes14.class;
            case "bytes15":
                return Bytes15.class;
            case "bytes16":
                return Bytes16.class;
            case "bytes17":
                return Bytes17.class;
            case "bytes18":
                return Bytes18.class;
            case "bytes19":
                return Bytes19.class;
            case "bytes20":
                return Bytes20.class;
            case "bytes21":
                return Bytes21.class;
            case "bytes22":
                return Bytes22.class;
            case "bytes23":
                return Bytes23.class;
            case "bytes24":
                return Bytes24.class;
            case "bytes25":
                return Bytes25.class;
            case "bytes26":
                return Bytes26.class;
            case "bytes27":
                return Bytes27.class;
            case "bytes28":
                return Bytes28.class;
            case "bytes29":
                return Bytes29.class;
            case "bytes30":
                return Bytes30.class;
            case "bytes31":
                return Bytes31.class;
            case "bytes32":
                return Bytes32.class;
            default:
                throw new UnsupportedOperationException("FOO Unsupported type encountered: "
                        + type);
        }
    }

    public static String getTypeAString(Class<? extends Type> type) {
        if (Utf8String.class.equals(type)) {
            return "string";
        } else if (DynamicBytes.class.equals(type)) {
            return "bytes";
        }
        else {
            return type.getSimpleName().toLowerCase();
        }
    }
}
