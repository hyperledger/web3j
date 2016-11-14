package org.web3j.abi.datatypes.generated;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Utf8String;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesMapperGenerator} to update.</p>
 */
public final class AbiTypes {
  private AbiTypes() {
  }

  public static Class<?> getType(String type) {
    if (type == null || type.equals("")) {
      throw new UnsupportedOperationException("Type must not be empty or null");
    }
    else if ("address".equals(type)) {
      return Address.class;
    }
    else if ("bool".equals(type)) {
      return Bool.class;
    }
    else if ("string".equals(type)) {
      return Utf8String.class;
    }
    else if ("bytes".equals(type)) {
      return DynamicBytes.class;
    }
    else if ("uint8".equals(type)) {
      return Uint8.class;
    }
    else if ("int8".equals(type)) {
      return Int8.class;
    }
    else if ("uint16".equals(type)) {
      return Uint16.class;
    }
    else if ("int16".equals(type)) {
      return Int16.class;
    }
    else if ("uint24".equals(type)) {
      return Uint24.class;
    }
    else if ("int24".equals(type)) {
      return Int24.class;
    }
    else if ("uint32".equals(type)) {
      return Uint32.class;
    }
    else if ("int32".equals(type)) {
      return Int32.class;
    }
    else if ("uint40".equals(type)) {
      return Uint40.class;
    }
    else if ("int40".equals(type)) {
      return Int40.class;
    }
    else if ("uint48".equals(type)) {
      return Uint48.class;
    }
    else if ("int48".equals(type)) {
      return Int48.class;
    }
    else if ("uint56".equals(type)) {
      return Uint56.class;
    }
    else if ("int56".equals(type)) {
      return Int56.class;
    }
    else if ("uint64".equals(type)) {
      return Uint64.class;
    }
    else if ("int64".equals(type)) {
      return Int64.class;
    }
    else if ("uint72".equals(type)) {
      return Uint72.class;
    }
    else if ("int72".equals(type)) {
      return Int72.class;
    }
    else if ("uint80".equals(type)) {
      return Uint80.class;
    }
    else if ("int80".equals(type)) {
      return Int80.class;
    }
    else if ("uint88".equals(type)) {
      return Uint88.class;
    }
    else if ("int88".equals(type)) {
      return Int88.class;
    }
    else if ("uint96".equals(type)) {
      return Uint96.class;
    }
    else if ("int96".equals(type)) {
      return Int96.class;
    }
    else if ("uint104".equals(type)) {
      return Uint104.class;
    }
    else if ("int104".equals(type)) {
      return Int104.class;
    }
    else if ("uint112".equals(type)) {
      return Uint112.class;
    }
    else if ("int112".equals(type)) {
      return Int112.class;
    }
    else if ("uint120".equals(type)) {
      return Uint120.class;
    }
    else if ("int120".equals(type)) {
      return Int120.class;
    }
    else if ("uint128".equals(type)) {
      return Uint128.class;
    }
    else if ("int128".equals(type)) {
      return Int128.class;
    }
    else if ("uint136".equals(type)) {
      return Uint136.class;
    }
    else if ("int136".equals(type)) {
      return Int136.class;
    }
    else if ("uint144".equals(type)) {
      return Uint144.class;
    }
    else if ("int144".equals(type)) {
      return Int144.class;
    }
    else if ("uint152".equals(type)) {
      return Uint152.class;
    }
    else if ("int152".equals(type)) {
      return Int152.class;
    }
    else if ("uint160".equals(type)) {
      return Uint160.class;
    }
    else if ("int160".equals(type)) {
      return Int160.class;
    }
    else if ("uint168".equals(type)) {
      return Uint168.class;
    }
    else if ("int168".equals(type)) {
      return Int168.class;
    }
    else if ("uint176".equals(type)) {
      return Uint176.class;
    }
    else if ("int176".equals(type)) {
      return Int176.class;
    }
    else if ("uint184".equals(type)) {
      return Uint184.class;
    }
    else if ("int184".equals(type)) {
      return Int184.class;
    }
    else if ("uint192".equals(type)) {
      return Uint192.class;
    }
    else if ("int192".equals(type)) {
      return Int192.class;
    }
    else if ("uint200".equals(type)) {
      return Uint200.class;
    }
    else if ("int200".equals(type)) {
      return Int200.class;
    }
    else if ("uint208".equals(type)) {
      return Uint208.class;
    }
    else if ("int208".equals(type)) {
      return Int208.class;
    }
    else if ("uint216".equals(type)) {
      return Uint216.class;
    }
    else if ("int216".equals(type)) {
      return Int216.class;
    }
    else if ("uint224".equals(type)) {
      return Uint224.class;
    }
    else if ("int224".equals(type)) {
      return Int224.class;
    }
    else if ("uint232".equals(type)) {
      return Uint232.class;
    }
    else if ("int232".equals(type)) {
      return Int232.class;
    }
    else if ("uint240".equals(type)) {
      return Uint240.class;
    }
    else if ("int240".equals(type)) {
      return Int240.class;
    }
    else if ("uint248".equals(type)) {
      return Uint248.class;
    }
    else if ("int248".equals(type)) {
      return Int248.class;
    }
    else if ("uint256".equals(type)) {
      return Uint256.class;
    }
    else if ("int256".equals(type)) {
      return Int256.class;
    }
    else if ("ufixed8x248".equals(type)) {
      return Ufixed8x248.class;
    }
    else if ("fixed8x248".equals(type)) {
      return Fixed8x248.class;
    }
    else if ("ufixed16x240".equals(type)) {
      return Ufixed16x240.class;
    }
    else if ("fixed16x240".equals(type)) {
      return Fixed16x240.class;
    }
    else if ("ufixed24x232".equals(type)) {
      return Ufixed24x232.class;
    }
    else if ("fixed24x232".equals(type)) {
      return Fixed24x232.class;
    }
    else if ("ufixed32x224".equals(type)) {
      return Ufixed32x224.class;
    }
    else if ("fixed32x224".equals(type)) {
      return Fixed32x224.class;
    }
    else if ("ufixed40x216".equals(type)) {
      return Ufixed40x216.class;
    }
    else if ("fixed40x216".equals(type)) {
      return Fixed40x216.class;
    }
    else if ("ufixed48x208".equals(type)) {
      return Ufixed48x208.class;
    }
    else if ("fixed48x208".equals(type)) {
      return Fixed48x208.class;
    }
    else if ("ufixed56x200".equals(type)) {
      return Ufixed56x200.class;
    }
    else if ("fixed56x200".equals(type)) {
      return Fixed56x200.class;
    }
    else if ("ufixed64x192".equals(type)) {
      return Ufixed64x192.class;
    }
    else if ("fixed64x192".equals(type)) {
      return Fixed64x192.class;
    }
    else if ("ufixed72x184".equals(type)) {
      return Ufixed72x184.class;
    }
    else if ("fixed72x184".equals(type)) {
      return Fixed72x184.class;
    }
    else if ("ufixed80x176".equals(type)) {
      return Ufixed80x176.class;
    }
    else if ("fixed80x176".equals(type)) {
      return Fixed80x176.class;
    }
    else if ("ufixed88x168".equals(type)) {
      return Ufixed88x168.class;
    }
    else if ("fixed88x168".equals(type)) {
      return Fixed88x168.class;
    }
    else if ("ufixed96x160".equals(type)) {
      return Ufixed96x160.class;
    }
    else if ("fixed96x160".equals(type)) {
      return Fixed96x160.class;
    }
    else if ("ufixed104x152".equals(type)) {
      return Ufixed104x152.class;
    }
    else if ("fixed104x152".equals(type)) {
      return Fixed104x152.class;
    }
    else if ("ufixed112x144".equals(type)) {
      return Ufixed112x144.class;
    }
    else if ("fixed112x144".equals(type)) {
      return Fixed112x144.class;
    }
    else if ("ufixed120x136".equals(type)) {
      return Ufixed120x136.class;
    }
    else if ("fixed120x136".equals(type)) {
      return Fixed120x136.class;
    }
    else if ("ufixed128x128".equals(type)) {
      return Ufixed128x128.class;
    }
    else if ("fixed128x128".equals(type)) {
      return Fixed128x128.class;
    }
    else if ("ufixed136x120".equals(type)) {
      return Ufixed136x120.class;
    }
    else if ("fixed136x120".equals(type)) {
      return Fixed136x120.class;
    }
    else if ("ufixed144x112".equals(type)) {
      return Ufixed144x112.class;
    }
    else if ("fixed144x112".equals(type)) {
      return Fixed144x112.class;
    }
    else if ("ufixed152x104".equals(type)) {
      return Ufixed152x104.class;
    }
    else if ("fixed152x104".equals(type)) {
      return Fixed152x104.class;
    }
    else if ("ufixed160x96".equals(type)) {
      return Ufixed160x96.class;
    }
    else if ("fixed160x96".equals(type)) {
      return Fixed160x96.class;
    }
    else if ("ufixed168x88".equals(type)) {
      return Ufixed168x88.class;
    }
    else if ("fixed168x88".equals(type)) {
      return Fixed168x88.class;
    }
    else if ("ufixed176x80".equals(type)) {
      return Ufixed176x80.class;
    }
    else if ("fixed176x80".equals(type)) {
      return Fixed176x80.class;
    }
    else if ("ufixed184x72".equals(type)) {
      return Ufixed184x72.class;
    }
    else if ("fixed184x72".equals(type)) {
      return Fixed184x72.class;
    }
    else if ("ufixed192x64".equals(type)) {
      return Ufixed192x64.class;
    }
    else if ("fixed192x64".equals(type)) {
      return Fixed192x64.class;
    }
    else if ("ufixed200x56".equals(type)) {
      return Ufixed200x56.class;
    }
    else if ("fixed200x56".equals(type)) {
      return Fixed200x56.class;
    }
    else if ("ufixed208x48".equals(type)) {
      return Ufixed208x48.class;
    }
    else if ("fixed208x48".equals(type)) {
      return Fixed208x48.class;
    }
    else if ("ufixed216x40".equals(type)) {
      return Ufixed216x40.class;
    }
    else if ("fixed216x40".equals(type)) {
      return Fixed216x40.class;
    }
    else if ("ufixed224x32".equals(type)) {
      return Ufixed224x32.class;
    }
    else if ("fixed224x32".equals(type)) {
      return Fixed224x32.class;
    }
    else if ("ufixed232x24".equals(type)) {
      return Ufixed232x24.class;
    }
    else if ("fixed232x24".equals(type)) {
      return Fixed232x24.class;
    }
    else if ("ufixed240x16".equals(type)) {
      return Ufixed240x16.class;
    }
    else if ("fixed240x16".equals(type)) {
      return Fixed240x16.class;
    }
    else if ("ufixed248x8".equals(type)) {
      return Ufixed248x8.class;
    }
    else if ("fixed248x8".equals(type)) {
      return Fixed248x8.class;
    }
    else if ("bytes1".equals(type)) {
      return Bytes1.class;
    }
    else if ("bytes2".equals(type)) {
      return Bytes2.class;
    }
    else if ("bytes3".equals(type)) {
      return Bytes3.class;
    }
    else if ("bytes4".equals(type)) {
      return Bytes4.class;
    }
    else if ("bytes5".equals(type)) {
      return Bytes5.class;
    }
    else if ("bytes6".equals(type)) {
      return Bytes6.class;
    }
    else if ("bytes7".equals(type)) {
      return Bytes7.class;
    }
    else if ("bytes8".equals(type)) {
      return Bytes8.class;
    }
    else if ("bytes9".equals(type)) {
      return Bytes9.class;
    }
    else if ("bytes10".equals(type)) {
      return Bytes10.class;
    }
    else if ("bytes11".equals(type)) {
      return Bytes11.class;
    }
    else if ("bytes12".equals(type)) {
      return Bytes12.class;
    }
    else if ("bytes13".equals(type)) {
      return Bytes13.class;
    }
    else if ("bytes14".equals(type)) {
      return Bytes14.class;
    }
    else if ("bytes15".equals(type)) {
      return Bytes15.class;
    }
    else if ("bytes16".equals(type)) {
      return Bytes16.class;
    }
    else if ("bytes17".equals(type)) {
      return Bytes17.class;
    }
    else if ("bytes18".equals(type)) {
      return Bytes18.class;
    }
    else if ("bytes19".equals(type)) {
      return Bytes19.class;
    }
    else if ("bytes20".equals(type)) {
      return Bytes20.class;
    }
    else if ("bytes21".equals(type)) {
      return Bytes21.class;
    }
    else if ("bytes22".equals(type)) {
      return Bytes22.class;
    }
    else if ("bytes23".equals(type)) {
      return Bytes23.class;
    }
    else if ("bytes24".equals(type)) {
      return Bytes24.class;
    }
    else if ("bytes25".equals(type)) {
      return Bytes25.class;
    }
    else if ("bytes26".equals(type)) {
      return Bytes26.class;
    }
    else if ("bytes27".equals(type)) {
      return Bytes27.class;
    }
    else if ("bytes28".equals(type)) {
      return Bytes28.class;
    }
    else if ("bytes29".equals(type)) {
      return Bytes29.class;
    }
    else if ("bytes30".equals(type)) {
      return Bytes30.class;
    }
    else if ("bytes31".equals(type)) {
      return Bytes31.class;
    }
    else if ("bytes32".equals(type)) {
      return Bytes32.class;
    }
    else  {
      throw new UnsupportedOperationException("Unsupported type encountered");
    }
  }
}
