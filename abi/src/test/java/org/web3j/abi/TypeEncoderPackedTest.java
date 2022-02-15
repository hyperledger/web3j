package org.web3j.abi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;
import org.web3j.abi.datatypes.primitive.Byte;
import org.web3j.abi.datatypes.primitive.Char;
import org.web3j.abi.datatypes.primitive.Long;
import org.web3j.abi.datatypes.primitive.Short;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TypeEncoderPackedTest {

    @Test
    public void testAddressEncodePacked() {
        Address address = new Address("0x663e27AdC18d862dA9A82f060310621D379e469a");
        assertEquals(address.getTypeAsString(), ("address"));
        assertEquals(
                "663e27adc18d862da9a82f060310621d379e469a",
                TypeEncoder.encodePacked(address));


        Address addressLong = new Address(
                        256, "0xa04462684b510796c186d19abfa6929742f79394583d6efb1243bbb473f21d9f");
        assertEquals(
                "a04462684b510796c186d19abfa6929742f79394583d6efb1243bbb473f21d9f",
                TypeEncoder.encodePacked(addressLong));
    }

    @Test
    public void testBoolEncodePacked() {
        assertEquals(TypeEncoder.encodePacked(new Bool(false)), ("00"));
        assertEquals(TypeEncoder.encodePacked(new Bool(true)), ("01"));
    }

    @Test
    public void testUintEncodePacked() {
        Uint zero8 = new Uint8(BigInteger.ZERO);
        assertEquals(
                "00",
                TypeEncoder.encodePacked(zero8));
        Uint max8 = new Uint8(255);
        assertEquals(
                "ff",
                TypeEncoder.encodePacked(max8));

        Uint zero16 = new Uint16(BigInteger.ZERO);
        assertEquals(
                "0000",
                TypeEncoder.encodePacked(zero16));
        Uint max16 = new Uint16(65535);
        assertEquals(
                "ffff",
                TypeEncoder.encodePacked(max16));

        Uint zero24 = new Uint24(BigInteger.ZERO);
        assertEquals(
                "000000",
                TypeEncoder.encodePacked(zero24));
        Uint max24 = new Uint24(16777215);
        assertEquals(
                "ffffff",
                TypeEncoder.encodePacked(max24));

        Uint zero32 = new Uint32(BigInteger.ZERO);
        assertEquals(
                "00000000",
                TypeEncoder.encodePacked(zero32));
        Uint max32 = new Uint32(BigInteger.valueOf(4294967295L));
        assertEquals(
                "ffffffff",
                TypeEncoder.encodePacked(max32));

        Uint zero40 = new Uint40(BigInteger.ZERO);
        assertEquals(
                "0000000000",
                TypeEncoder.encodePacked(zero40));
        Uint max40 = new Uint40(BigInteger.valueOf(1099511627775L));
        assertEquals(
                "ffffffffff",
                TypeEncoder.encodePacked(max40));

        Uint zero48 = new Uint48(BigInteger.ZERO);
        assertEquals(
                "000000000000",
                TypeEncoder.encodePacked(zero48));
        Uint max48 = new Uint48(BigInteger.valueOf(281474976710655L));
        assertEquals(
                "ffffffffffff",
                TypeEncoder.encodePacked(max48));

        Uint zero56 = new Uint56(BigInteger.ZERO);
        assertEquals(
                "00000000000000",
                TypeEncoder.encodePacked(zero56));
        Uint max56 = new Uint56(BigInteger.valueOf(72057594037927935L));
        assertEquals(
                "ffffffffffffff",
                TypeEncoder.encodePacked(max56));

        Uint zero64 = new Uint64(BigInteger.ZERO);
        assertEquals(
                "0000000000000000",
                TypeEncoder.encodePacked(zero64));

        Uint maxLong = new Uint64(BigInteger.valueOf(java.lang.Long.MAX_VALUE));
        assertEquals(
                "7fffffffffffffff",
                TypeEncoder.encodePacked(maxLong));

        Uint maxValue64 =
                new Uint(
                        new BigInteger(
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                                16));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(maxValue64));

        Uint largeValue =
                new Uint(
                        new BigInteger(
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe",
                                16));
        assertEquals(
                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe",
                TypeEncoder.encodePacked(largeValue));

        Uint zero72 = new Uint72(BigInteger.ZERO);
        assertEquals(
                "000000000000000000",
                TypeEncoder.encodePacked(zero72));
        Uint max72 = new Uint72(new BigInteger("4722366482869645213695"));
        assertEquals(
                "ffffffffffffffffff",
                TypeEncoder.encodePacked(max72));

        Uint zero80 = new Uint80(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000",
                TypeEncoder.encodePacked(zero80));
        Uint max80 = new Uint80(new BigInteger("1208925819614629174706175"));
        assertEquals(
                "ffffffffffffffffffff",
                TypeEncoder.encodePacked(max80));

        Uint zero88 = new Uint88(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000",
                TypeEncoder.encodePacked(zero88));
        Uint max88 = new Uint88(new BigInteger("309485009821345068724781055"));
        assertEquals(
                "ffffffffffffffffffffff",
                TypeEncoder.encodePacked(max88));

        Uint zero96 = new Uint96(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000",
                TypeEncoder.encodePacked(zero96));
        Uint max96 = new Uint96(new BigInteger("79228162514264337593543950335"));
        assertEquals(
                "ffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max96));

        Uint zero104 = new Uint104(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000",
                TypeEncoder.encodePacked(zero104));
        Uint max104 = new Uint104(new BigInteger("20282409603651670423947251286015"));
        assertEquals(
                "ffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max104));

        Uint zero112 = new Uint112(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000000000",
                TypeEncoder.encodePacked(zero112));
        Uint max112 = new Uint112(new BigInteger("5192296858534827628530496329220095"));
        assertEquals(
                "ffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max112));

        Uint zero120 = new Uint120(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000000000",
                TypeEncoder.encodePacked(zero120));
        Uint max120 = new Uint120(new BigInteger("1329227995784915872903807060280344575"));
        assertEquals(
                "ffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max120));

        Uint zero128 = new Uint128(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000000000",
                TypeEncoder.encodePacked(zero128));
        Uint max128 = new Uint128(new BigInteger("340282366920938463463374607431768211455"));
        assertEquals(
                "ffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max128));

        Uint zero136 = new Uint136(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero136));
        Uint max136 = new Uint136(new BigInteger("87112285931760246646623899502532662132735"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max136));

        Uint zero144 = new Uint144(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero144));
        Uint max144 = new Uint144(new BigInteger("22300745198530623141535718272648361505980415"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max144));

        Uint zero152 = new Uint152(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero152));
        Uint max152 = new Uint152(new BigInteger("5708990770823839524233143877797980545530986495"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max152));

        Uint zero160 = new Uint160(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero160));
        Uint max160 =
                new Uint160(new BigInteger("1461501637330902918203684832716283019655932542975"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max160));

        Uint zero168 = new Uint168(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero168));
        Uint max168 =
                new Uint168(new BigInteger("374144419156711147060143317175368453031918731001855"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max168));

        Uint zero176 = new Uint176(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero176));
        Uint max176 =
                new Uint176(
                        new BigInteger("95780971304118053647396689196894323976171195136475135"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max176));

        Uint zero184 = new Uint184(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero184));
        Uint max184 =
                new Uint184(
                        new BigInteger("24519928653854221733733552434404946937899825954937634815"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max184));

        Uint zero192 = new Uint192(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero192));
        Uint max192 =
                new Uint192(
                        new BigInteger(
                                "6277101735386680763835789423207666416102355444464034512895"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max192));

        Uint zero200 = new Uint200(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero200));
        Uint max200 =
                new Uint200(
                        new BigInteger(
                                "1606938044258990275541962092341162602522202993782792835301375"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max200));

        Uint zero208 = new Uint208(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero208));
        Uint max208 =
                new Uint208(
                        new BigInteger(
                                "411376139330301510538742295639337626245683966408394965837152255"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max208));

        Uint zero216 = new Uint216(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero216));
        Uint max216 =
                new Uint216(
                        new BigInteger(
                                "105312291668557186697918027683670432318895095400549111254310977535"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max216));

        Uint zero224 = new Uint224(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero224));
        Uint max224 =
                new Uint224(
                        new BigInteger(
                                "26959946667150639794667015087019630673637144422540572481103610249215"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max224));

        Uint zero232 = new Uint232(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero232));
        Uint max232 =
                new Uint232(
                        new BigInteger(
                                "6901746346790563787434755862277025452451108972170386555162524223799295"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max232));

        Uint zero240 = new Uint240(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero240));
        Uint max240 =
                new Uint240(
                        new BigInteger(
                                "1766847064778384329583297500742918515827483896875618958121606201292619775"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max240));

        Uint zero248 = new Uint248(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero248));
        Uint max248 =
                new Uint248(
                        new BigInteger(
                                "452312848583266388373324160190187140051835877600158453279131187530910662655"));
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max248));

    }

    @Test
    public void testIntEncodePacked() {
        Int zero8 = new Int8(BigInteger.ZERO);
        assertEquals(
                "00",
                TypeEncoder.encodePacked(zero8));

        Int max8 = new Int8(BigInteger.valueOf(127));
        assertEquals(
                "7f",
                TypeEncoder.encodePacked(max8));

        Int min8 = new Int8(BigInteger.valueOf(-128));
        assertEquals(
                "80",
                TypeEncoder.encodePacked(min8));

        Int zero16 = new Int16(BigInteger.ZERO);
        assertEquals(
                "0000",
                TypeEncoder.encodePacked(zero16));

        Int max16 = new Int16(BigInteger.valueOf(32767));
        assertEquals(
                "7fff",
                TypeEncoder.encodePacked(max16));

        Int min16 = new Int16(BigInteger.valueOf(-32768));
        assertEquals(
                "8000",
                TypeEncoder.encodePacked(min16));

        Int zero24 = new Int24(BigInteger.ZERO);
        assertEquals(
                "000000",
                TypeEncoder.encodePacked(zero24));

        Int max24 = new Int24(BigInteger.valueOf(8388607));
        assertEquals(
                "7fffff",
                TypeEncoder.encodePacked(max24));

        Int min24 = new Int24(BigInteger.valueOf(-8388608));
        assertEquals(
                "800000",
                TypeEncoder.encodePacked(min24));

        Int zero32 = new Int32(BigInteger.ZERO);
        assertEquals(
                "00000000",
                TypeEncoder.encodePacked(zero32));

        Int max32 = new Int32(BigInteger.valueOf(2147483647));
        assertEquals(
                "7fffffff",
                TypeEncoder.encodePacked(max32));

        Int min32 = new Int32(BigInteger.valueOf(-2147483648));
        assertEquals(
                "80000000",
                TypeEncoder.encodePacked(min32));

        Int zero40 = new Int40(BigInteger.ZERO);
        assertEquals(
                "0000000000",
                TypeEncoder.encodePacked(zero40));

        Int max40 = new Int40(BigInteger.valueOf(549755813887L));
        assertEquals(
                "7fffffffff",
                TypeEncoder.encodePacked(max40));

        Int min40 = new Int40(BigInteger.valueOf(-549755813888L));
        assertEquals(
                "8000000000",
                TypeEncoder.encodePacked(min40));

        Int zero48 = new Int48(BigInteger.ZERO);
        assertEquals(
                "000000000000",
                TypeEncoder.encodePacked(zero48));

        Int max48 = new Int48(BigInteger.valueOf(140737488355327L));
        assertEquals(
                "7fffffffffff",
                TypeEncoder.encodePacked(max48));

        Int min48 = new Int48(BigInteger.valueOf(-140737488355328L));
        assertEquals(
                "800000000000",
                TypeEncoder.encodePacked(min48));

        Int zero56 = new Int56(BigInteger.ZERO);
        assertEquals(
                "00000000000000",
                TypeEncoder.encodePacked(zero56));

        Int max56 = new Int56(BigInteger.valueOf(36028797018963967L));
        assertEquals(
                "7fffffffffffff",
                TypeEncoder.encodePacked(max56));

        Int min56 = new Int56(BigInteger.valueOf(-36028797018963968L));
        assertEquals(
                "80000000000000",
                TypeEncoder.encodePacked(min56));

        Int zero64 = new Int64(BigInteger.ZERO);
        assertEquals(
                "0000000000000000",
                TypeEncoder.encodePacked(zero64));

        Int max64 = new Int64(BigInteger.valueOf(java.lang.Long.MAX_VALUE));
        assertEquals(
                "7fffffffffffffff",
                TypeEncoder.encodePacked(max64));

        Int min64 = new Int64(BigInteger.valueOf(java.lang.Long.MIN_VALUE));
        assertEquals(
                "8000000000000000",
                TypeEncoder.encodePacked(min64));

        Int zero72 = new Int72(BigInteger.ZERO);
        assertEquals(
                "000000000000000000",
                TypeEncoder.encodePacked(zero72));

        Int max72 = new Int72(new BigInteger("2361183241434822606847"));
        assertEquals(
                "7fffffffffffffffff",
                TypeEncoder.encodePacked(max72));

        Int min72 = new Int72(new BigInteger("-2361183241434822606848"));
        assertEquals(
                "800000000000000000",
                TypeEncoder.encodePacked(min72));

        Int zero80 = new Int80(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000",
                TypeEncoder.encodePacked(zero80));

        Int max80 = new Int80(new BigInteger("604462909807314587353087"));
        assertEquals(
                "7fffffffffffffffffff",
                TypeEncoder.encodePacked(max80));

        Int min80 = new Int80(new BigInteger("-604462909807314587353088"));
        assertEquals(
                "80000000000000000000",
                TypeEncoder.encodePacked(min80));

        Int zero88 = new Int88(BigInteger.ZERO);
        assertEquals(
                "0000000000000000000000",
                TypeEncoder.encodePacked(zero88));

        Int max88 = new Int88(new BigInteger("154742504910672534362390527"));
        assertEquals(
                "7fffffffffffffffffffff",
                TypeEncoder.encodePacked(max88));

        Int min88 = new Int88(new BigInteger("-154742504910672534362390528"));
        assertEquals(
                "8000000000000000000000",
                TypeEncoder.encodePacked(min88));

        Int zero96 = new Int96(BigInteger.ZERO);
        assertEquals(
                "000000000000000000000000",
                TypeEncoder.encodePacked(zero96));

        Int max96 = new Int96(new BigInteger("39614081257132168796771975167"));
        assertEquals(
                "7fffffffffffffffffffffff",
                TypeEncoder.encodePacked(max96));

        Int min96 = new Int96(new BigInteger("-39614081257132168796771975168"));
        assertEquals(
                "800000000000000000000000",
                TypeEncoder.encodePacked(min96));

        Int zero104 = new Int104(BigInteger.ZERO);
        assertEquals(
                "00000000000000000000000000",
                TypeEncoder.encodePacked(zero104));

        Int max104 = new Int104(new BigInteger("10141204801825835211973625643007"));
        assertEquals(
                "7fffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max104));

        Int min104 = new Int104(new BigInteger("-10141204801825835211973625643008"));
        assertEquals(
                "80000000000000000000000000",
                TypeEncoder.encodePacked(min104));

        Int zero112 = new Int112(BigInteger.ZERO);

        assertEquals(
                "0000000000000000000000000000",
                TypeEncoder.encodePacked(zero112));

        Int max112 = new Int112(new BigInteger("2596148429267413814265248164610047"));
        assertEquals(
                "7fffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max112));

        Int min112 = new Int112(new BigInteger("-2596148429267413814265248164610048"));
        assertEquals(
                "8000000000000000000000000000",
                TypeEncoder.encodePacked(min112));

        Int zero120 = new Int120(BigInteger.ZERO);

        assertEquals(
                "000000000000000000000000000000",
                TypeEncoder.encodePacked(zero120));

        Int max120 = new Int120(new BigInteger("664613997892457936451903530140172287"));
        assertEquals(
                "7fffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max120));

        Int min120 = new Int120(new BigInteger("-664613997892457936451903530140172288"));
        assertEquals(
                "800000000000000000000000000000",
                TypeEncoder.encodePacked(min120));

        Int zero128 = new Int128(BigInteger.ZERO);

        assertEquals(
                "00000000000000000000000000000000",
                TypeEncoder.encodePacked(zero128));

        Int max128 = new Int128(new BigInteger("170141183460469231731687303715884105727"));
        assertEquals(
                "7fffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max128));

        Int min128 = new Int128(new BigInteger("-170141183460469231731687303715884105728"));
        assertEquals(
                "80000000000000000000000000000000",
                TypeEncoder.encodePacked(min128));

        Int zero136 = new Int136(BigInteger.ZERO);

        assertEquals(
                "0000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero136));

        Int max136 = new Int136(new BigInteger("43556142965880123323311949751266331066367"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max136));

        Int min136 = new Int136(new BigInteger("-43556142965880123323311949751266331066368"));
        assertEquals(
                "8000000000000000000000000000000000",
                TypeEncoder.encodePacked(min136));

        Int zero144 = new Int144(BigInteger.ZERO);

        assertEquals(
                "000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero144));

        Int max144 = new Int144(new BigInteger("11150372599265311570767859136324180752990207"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max144));

        Int min144 = new Int144(new BigInteger("-11150372599265311570767859136324180752990208"));
        assertEquals(
                "800000000000000000000000000000000000",
                TypeEncoder.encodePacked(min144));

        Int zero152 = new Int152(BigInteger.ZERO);

        assertEquals(
                "00000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero152));

        Int max152 = new Int152(new BigInteger("2854495385411919762116571938898990272765493247"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max152));

        Int min152 = new Int152(new BigInteger("-2854495385411919762116571938898990272765493248"));
        assertEquals(
                "80000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min152));

        Int zero160 = new Int160(BigInteger.ZERO);

        assertEquals(
                "0000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero160));

        Int max160 = new Int160(new BigInteger("730750818665451459101842416358141509827966271487"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max160));

        Int min160 =
                new Int160(new BigInteger("-730750818665451459101842416358141509827966271488"));
        assertEquals(
                "8000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min160));

        Int zero168 = new Int168(BigInteger.ZERO);

        assertEquals(
                "000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero168));

        Int max168 =
                new Int168(new BigInteger("187072209578355573530071658587684226515959365500927"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max168));

        Int min168 =
                new Int168(new BigInteger("-187072209578355573530071658587684226515959365500928"));
        assertEquals(
                "800000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min168));

        Int zero176 = new Int176(BigInteger.ZERO);

        assertEquals(
                "00000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero176));

        Int max176 =
                new Int176(new BigInteger("47890485652059026823698344598447161988085597568237567"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max176));

        Int min176 =
                new Int176(
                        new BigInteger("-47890485652059026823698344598447161988085597568237568"));
        assertEquals(
                "80000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min176));

        Int zero184 = new Int184(BigInteger.ZERO);

        assertEquals(
                "0000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero184));

        Int max184 =
                new Int184(
                        new BigInteger("12259964326927110866866776217202473468949912977468817407"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max184));

        Int min184 =
                new Int184(
                        new BigInteger(
                                "-12259964326927110866866776217202473468949912977468817408"));
        assertEquals(
                "8000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min184));

        Int zero192 = new Int192(BigInteger.ZERO);

        assertEquals(
                "000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero192));

        Int max192 =
                new Int192(
                        new BigInteger(
                                "3138550867693340381917894711603833208051177722232017256447"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max192));

        Int min192 =
                new Int192(
                        new BigInteger(
                                "-3138550867693340381917894711603833208051177722232017256448"));
        assertEquals(
                "800000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min192));

        Int zero200 = new Int200(BigInteger.ZERO);

        assertEquals(
                "00000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero200));

        Int max200 =
                new Int200(
                        new BigInteger(
                                "803469022129495137770981046170581301261101496891396417650687"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max200));

        Int min200 =
                new Int200(
                        new BigInteger(
                                "-803469022129495137770981046170581301261101496891396417650688"));
        assertEquals(
                "80000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min200));

        Int zero208 = new Int208(BigInteger.ZERO);

        assertEquals(
                "0000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero208));

        Int max208 =
                new Int208(
                        new BigInteger(
                                "205688069665150755269371147819668813122841983204197482918576127"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max208));

        Int min208 =
                new Int208(
                        new BigInteger(
                                "-205688069665150755269371147819668813122841983204197482918576128"));
        assertEquals(
                "8000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min208));

        Int zero216 = new Int216(BigInteger.ZERO);

        assertEquals(
                "000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero216));

        Int max216 =
                new Int216(
                        new BigInteger(
                                "52656145834278593348959013841835216159447547700274555627155488767"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max216));

        Int min216 =
                new Int216(
                        new BigInteger(
                                "-52656145834278593348959013841835216159447547700274555627155488768"));
        assertEquals(
                "800000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min216));

        Int zero224 = new Int224(BigInteger.ZERO);

        assertEquals(
                "00000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero224));

        Int max224 =
                new Int224(
                        new BigInteger(
                                "13479973333575319897333507543509815336818572211270286240551805124607"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max224));

        Int min224 =
                new Int224(
                        new BigInteger(
                                "-13479973333575319897333507543509815336818572211270286240551805124608"));
        assertEquals(
                "80000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min224));

        Int zero232 = new Int232(BigInteger.ZERO);

        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero232));

        Int max232 =
                new Int232(
                        new BigInteger(
                                "3450873173395281893717377931138512726225554486085193277581262111899647"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max232));

        Int min232 =
                new Int232(
                        new BigInteger(
                                "-3450873173395281893717377931138512726225554486085193277581262111899648"));
        assertEquals(
                "8000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min232));

        Int zero240 = new Int240(BigInteger.ZERO);

        assertEquals(
                "000000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero240));

        Int max240 =
                new Int240(
                        new BigInteger(
                                "883423532389192164791648750371459257913741948437809479060803100646309887"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max240));

        Int min240 =
                new Int240(
                        new BigInteger(
                                "-883423532389192164791648750371459257913741948437809479060803100646309888"));
        assertEquals(
                "800000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min240));

        Int zero248 = new Int248(BigInteger.ZERO);

        assertEquals(
                "00000000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(zero248));

        Int max248 =
                new Int248(
                        new BigInteger(
                                "226156424291633194186662080095093570025917938800079226639565593765455331327"));
        assertEquals(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                TypeEncoder.encodePacked(max248));

        Int min248 =
                new Int248(
                        new BigInteger(
                                "-226156424291633194186662080095093570025917938800079226639565593765455331328"));
        assertEquals(
                "80000000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(min248));
        Int minusOne = new Int(BigInteger.valueOf(-1));
        assertEquals(
                TypeEncoder.encodePacked(minusOne),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }

    @Test
    public void testStaticBytesEncodePacked() {
        Bytes staticBytes = new Bytes6(new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(
                TypeEncoder.encodePacked(staticBytes),
                ("000102030405"));

        Bytes empty = new Bytes1(new byte[]{0});
        assertEquals(
                TypeEncoder.encodePacked(empty),
                ("00"));

        Bytes ones = new Bytes1(new byte[]{127});
        assertEquals(
                TypeEncoder.encodePacked(ones),
                ("7f"));

        Bytes dave = new Bytes4("dave".getBytes());
        assertEquals(
                TypeEncoder.encodePacked(dave),
                ("64617665"));
    }

    @Test
    public void testUtf8StringEncodePacked() {
        Utf8String string = new Utf8String("Hello, world!");
        assertEquals(
                "48656c6c6f2c20776f726c6421",
                TypeEncoder.encodePacked(string));

        Utf8String largeString = new Utf8String("Very long string value for test!");
        assertEquals(
                "56657279206c6f6e6720737472696e672076616c756520666f72207465737421",
                TypeEncoder.encodePacked(largeString));

        Utf8String veryLargeString = new Utf8String("Very long string value for test!Very long string value for test!");
        assertEquals(
                "56657279206c6f6e6720737472696e672076616c756520666f722074657374"
                        + "2156657279206c6f6e6720737472696e672076616c756520666f72207465737421",
                TypeEncoder.encodePacked(veryLargeString));
    }

    @Test
    public void testStaticArrayEncodePacked() {
        StaticArray3<Uint16> array =
                new StaticArray3<>(
                        Uint16.class,
                        new Uint16(0x45),
                        new Uint16(0x7),
                        new Uint16(65535));

        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000045"
                        + "0000000000000000000000000000000000000000000000000000000000000007"
                        + "000000000000000000000000000000000000000000000000000000000000ffff",
                TypeEncoder.encodePacked(array)
        );
    }

    @Test
    public void testDynamicBytesEncodePacked() {
        DynamicBytes dynamicBytes = new DynamicBytes(new byte[]{0, 1, 2, 3, 4, 5});
        assertEquals(
                "0001020304050000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(dynamicBytes));

        DynamicBytes empty = new DynamicBytes(new byte[]{0});
        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(empty));

        DynamicBytes dave = new DynamicBytes("dave".getBytes());
        assertEquals(
                "6461766500000000000000000000000000000000000000000000000000000000",
                TypeEncoder.encodePacked(dave));

        DynamicBytes loremIpsum =
                new DynamicBytes(
                        ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
                                + "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim "
                                + "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex "
                                + "ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                                + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
                                + "sint occaecat cupidatat non proident, sunt in culpa qui officia "
                                + "deserunt mollit anim id est laborum.")
                                .getBytes());
        assertEquals(
                ("4c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73"
                        + "656374657475722061646970697363696e6720656c69742c2073656420646f20"
                        + "656975736d6f642074656d706f7220696e6369646964756e74207574206c6162"
                        + "6f726520657420646f6c6f7265206d61676e6120616c697175612e2055742065"
                        + "6e696d206164206d696e696d2076656e69616d2c2071756973206e6f73747275"
                        + "6420657865726369746174696f6e20756c6c616d636f206c61626f726973206e"
                        + "69736920757420616c697175697020657820656120636f6d6d6f646f20636f6e"
                        + "7365717561742e2044756973206175746520697275726520646f6c6f7220696e"
                        + "20726570726568656e646572697420696e20766f6c7570746174652076656c69"
                        + "7420657373652063696c6c756d20646f6c6f726520657520667567696174206e"
                        + "756c6c612070617269617475722e204578636570746575722073696e74206f63"
                        + "63616563617420637570696461746174206e6f6e2070726f6964656e742c2073"
                        + "756e7420696e2063756c706120717569206f666669636961206465736572756e"
                        + "74206d6f6c6c697420616e696d20696420657374206c61626f72756d2e000000"),
                TypeEncoder.encodePacked(loremIpsum));
    }

    @Test
    public void testDynamicArrayEncodePacked() {
        DynamicArray<Uint> array =
                new DynamicArray<>(
                        Uint.class,
                        new Uint(BigInteger.ONE),
                        new Uint(BigInteger.valueOf(2)),
                        new Uint(BigInteger.valueOf(3)));

        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000003",
                TypeEncoder.encodePacked(array));

        DynamicArray<Uint32> uints =
                new DynamicArray<>(
                        Uint32.class,
                        new Uint32(BigInteger.ONE),
                        new Uint32(BigInteger.valueOf(2)));

        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000002",
                TypeEncoder.encodePacked(uints));

        DynamicArray<Utf8String> strings =
                new DynamicArray<>(
                        Utf8String.class,
                        new Utf8String("one"),
                        new Utf8String("two"),
                        new Utf8String("three"));

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> TypeEncoder.encodePacked(strings));
    }

    @Test
    public void testPrimitiveByteEncodePacked() {
        assertEquals(
                "00",
                TypeEncoder.encodePacked(new Byte((byte) 0)));
        assertEquals(
                ("7f"),
                TypeEncoder.encodePacked(new Byte((byte) 127)));
    }

    @Test
    public void testPrimitiveCharEncodePacked() {
        assertEquals(
                "61",
                TypeEncoder.encodePacked(new Char('a')));
        assertEquals(
                "20",
                TypeEncoder.encodePacked(new Char(' ')));
    }

    @Test
    public void testPrimitiveIntEncodePacked() {
        assertEquals(
                "00000000",
                TypeEncoder.encodePacked(new org.web3j.abi.datatypes.primitive.Int(0)));

        assertEquals(
                "80000000",
                TypeEncoder.encodePacked(new org.web3j.abi.datatypes.primitive.Int(Integer.MIN_VALUE)));

        assertEquals(
                "7fffffff",
                TypeEncoder.encodePacked(new org.web3j.abi.datatypes.primitive.Int(Integer.MAX_VALUE)));
    }

    @Test
    public void testPrimitiveShortEncodePacked() {
        assertEquals(
                "0000",
                TypeEncoder.encodePacked(new Short((short) 0)));

        assertEquals(
                "8000",
                TypeEncoder.encodePacked(new Short(java.lang.Short.MIN_VALUE)));

        assertEquals(
                "7fff",
                TypeEncoder.encodePacked(new Short(java.lang.Short.MAX_VALUE)));
    }

    @Test
    public void testPrimitiveLongEncodePacked() {
        assertEquals(
                "0000000000000000",
                TypeEncoder.encodePacked(new Long(0)));

        assertEquals(
                "8000000000000000",
                TypeEncoder.encodePacked(new Long(java.lang.Long.MIN_VALUE)));

        assertEquals(
                "7fffffffffffffff",
                TypeEncoder.encodePacked(new Long(java.lang.Long.MAX_VALUE)));
    }

    @Test
    public void testPrimitiveFloatEncodePacked() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> TypeEncoder.encodePacked(new org.web3j.abi.datatypes.primitive.Float(0)));
    }

    @Test
    public void testPrimitiveDouble() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> TypeEncoder.encodePacked(new org.web3j.abi.datatypes.primitive.Double(0)));
    }
}