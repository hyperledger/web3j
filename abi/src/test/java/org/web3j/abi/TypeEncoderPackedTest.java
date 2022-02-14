package org.web3j.abi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TypeEncoderPackedTest {

    @Test
    public void testAddressEncodePacked() {
        Address address = new Address("0x663e27AdC18d862dA9A82f060310621D379e469a");
        assertEquals(address.getTypeAsString(), ("address"));
        assertEquals(
                TypeEncoder.encodePacked(address),
                ("663e27adc18d862da9a82f060310621d379e469a"));
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
                TypeEncoder.encodePacked(zero8),
                "00");
        Uint max8 = new Uint8(255);
        assertEquals(
                TypeEncoder.encodePacked(max8),
                "ff");

        Uint zero16 = new Uint16(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero16),
                "0000");
        Uint max16 = new Uint16(65535);
        assertEquals(
                TypeEncoder.encodePacked(max16),
                "ffff");

        Uint zero24 = new Uint24(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero24),
                "000000");
        Uint max24 = new Uint24(16777215);
        assertEquals(
                TypeEncoder.encodePacked(max24),
                "ffffff");

        Uint zero32 = new Uint32(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero32),
                "00000000");
        Uint max32 = new Uint32(BigInteger.valueOf(4294967295L));
        assertEquals(
                TypeEncoder.encodePacked(max32),
                "ffffffff");

        Uint zero40 = new Uint40(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero40),
                "0000000000");
        Uint max40 = new Uint40(BigInteger.valueOf(1099511627775L));
        assertEquals(
                TypeEncoder.encodePacked(max40),
                "ffffffffff");

        Uint zero48 = new Uint48(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero48),
                "000000000000");
        Uint max48 = new Uint48(BigInteger.valueOf(281474976710655L));
        assertEquals(
                TypeEncoder.encodePacked(max48),
                "ffffffffffff");

        Uint zero56 = new Uint56(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero56),
                "00000000000000");
        Uint max56 = new Uint56(BigInteger.valueOf(72057594037927935L));
        assertEquals(
                TypeEncoder.encodePacked(max56),
                "ffffffffffffff");

        Uint zero64 = new Uint64(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero64),
                ("0000000000000000"));

        Uint maxLong = new Uint64(BigInteger.valueOf(java.lang.Long.MAX_VALUE));
        assertEquals(
                TypeEncoder.encodePacked(maxLong),
                ("7fffffffffffffff"));

        Uint maxValue64 =
                new Uint(
                        new BigInteger(
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                                16));
        assertEquals(
                TypeEncoder.encodePacked(maxValue64),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));

        Uint largeValue =
                new Uint(
                        new BigInteger(
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe",
                                16));
        assertEquals(
                TypeEncoder.encodePacked(largeValue),
                ("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe"));

        Uint zero72 = new Uint72(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero72),
                "000000000000000000");
        Uint max72 = new Uint72(new BigInteger("4722366482869645213695"));
        assertEquals(
                TypeEncoder.encodePacked(max72),
                "ffffffffffffffffff");

        Uint zero80 = new Uint80(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero80),
                "00000000000000000000");
        Uint max80 = new Uint80(new BigInteger("1208925819614629174706175"));
        assertEquals(
                TypeEncoder.encodePacked(max80),
                "ffffffffffffffffffff");

        Uint zero88 = new Uint88(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero88),
                "0000000000000000000000");
        Uint max88 = new Uint88(new BigInteger("309485009821345068724781055"));
        assertEquals(
                TypeEncoder.encodePacked(max88),
                "ffffffffffffffffffffff");

        Uint zero96 = new Uint96(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero96),
                "000000000000000000000000");
        Uint max96 = new Uint96(new BigInteger("79228162514264337593543950335"));
        assertEquals(
                TypeEncoder.encodePacked(max96),
                "ffffffffffffffffffffffff");

        Uint zero104 = new Uint104(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero104),
                "00000000000000000000000000");
        Uint max104 = new Uint104(new BigInteger("20282409603651670423947251286015"));
        assertEquals(
                TypeEncoder.encodePacked(max104),
                "ffffffffffffffffffffffffff");

        Uint zero112 = new Uint112(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero112),
                "0000000000000000000000000000");
        Uint max112 = new Uint112(new BigInteger("5192296858534827628530496329220095"));
        assertEquals(
                TypeEncoder.encodePacked(max112),
                "ffffffffffffffffffffffffffff");

        Uint zero120 = new Uint120(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero120),
                "000000000000000000000000000000");
        Uint max120 = new Uint120(new BigInteger("1329227995784915872903807060280344575"));
        assertEquals(
                TypeEncoder.encodePacked(max120),
                "ffffffffffffffffffffffffffffff");

        Uint zero128 = new Uint128(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero128),
                "00000000000000000000000000000000");
        Uint max128 = new Uint128(new BigInteger("340282366920938463463374607431768211455"));
        assertEquals(
                TypeEncoder.encodePacked(max128),
                "ffffffffffffffffffffffffffffffff");

        Uint zero136 = new Uint136(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero136),
                "0000000000000000000000000000000000");
        Uint max136 = new Uint136(new BigInteger("87112285931760246646623899502532662132735"));
        assertEquals(
                TypeEncoder.encodePacked(max136),
                "ffffffffffffffffffffffffffffffffff");

        Uint zero144 = new Uint144(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero144),
                "000000000000000000000000000000000000");
        Uint max144 = new Uint144(new BigInteger("22300745198530623141535718272648361505980415"));
        assertEquals(
                TypeEncoder.encodePacked(max144),
                "ffffffffffffffffffffffffffffffffffff");

        Uint zero152 = new Uint152(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero152),
                "00000000000000000000000000000000000000");
        Uint max152 = new Uint152(new BigInteger("5708990770823839524233143877797980545530986495"));
        assertEquals(
                TypeEncoder.encodePacked(max152),
                "ffffffffffffffffffffffffffffffffffffff");

        Uint zero160 = new Uint160(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero160),
                "0000000000000000000000000000000000000000");
        Uint max160 =
                new Uint160(new BigInteger("1461501637330902918203684832716283019655932542975"));
        assertEquals(
                TypeEncoder.encodePacked(max160),
                "ffffffffffffffffffffffffffffffffffffffff");

        Uint zero168 = new Uint168(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero168),
                "000000000000000000000000000000000000000000");
        Uint max168 =
                new Uint168(new BigInteger("374144419156711147060143317175368453031918731001855"));
        assertEquals(
                TypeEncoder.encodePacked(max168),
                "ffffffffffffffffffffffffffffffffffffffffff");

        Uint zero176 = new Uint176(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero176),
                "00000000000000000000000000000000000000000000");
        Uint max176 =
                new Uint176(
                        new BigInteger("95780971304118053647396689196894323976171195136475135"));
        assertEquals(
                TypeEncoder.encodePacked(max176),
                "ffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero184 = new Uint184(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero184),
                "0000000000000000000000000000000000000000000000");
        Uint max184 =
                new Uint184(
                        new BigInteger("24519928653854221733733552434404946937899825954937634815"));
        assertEquals(
                TypeEncoder.encodePacked(max184),
                "ffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero192 = new Uint192(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero192),
                "000000000000000000000000000000000000000000000000");
        Uint max192 =
                new Uint192(
                        new BigInteger(
                                "6277101735386680763835789423207666416102355444464034512895"));
        assertEquals(
                TypeEncoder.encodePacked(max192),
                "ffffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero200 = new Uint200(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero200),
                "00000000000000000000000000000000000000000000000000");
        Uint max200 =
                new Uint200(
                        new BigInteger(
                                "1606938044258990275541962092341162602522202993782792835301375"));
        assertEquals(
                TypeEncoder.encodePacked(max200),
                "ffffffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero208 = new Uint208(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero208),
                "0000000000000000000000000000000000000000000000000000");
        Uint max208 =
                new Uint208(
                        new BigInteger(
                                "411376139330301510538742295639337626245683966408394965837152255"));
        assertEquals(
                TypeEncoder.encodePacked(max208),
                "ffffffffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero216 = new Uint216(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero216),
                "000000000000000000000000000000000000000000000000000000");
        Uint max216 =
                new Uint216(
                        new BigInteger(
                                "105312291668557186697918027683670432318895095400549111254310977535"));
        assertEquals(
                TypeEncoder.encodePacked(max216),
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero224 = new Uint224(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero224),
                "00000000000000000000000000000000000000000000000000000000");
        Uint max224 =
                new Uint224(
                        new BigInteger(
                                "26959946667150639794667015087019630673637144422540572481103610249215"));
        assertEquals(
                TypeEncoder.encodePacked(max224),
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero232 = new Uint232(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero232),
                "0000000000000000000000000000000000000000000000000000000000");
        Uint max232 =
                new Uint232(
                        new BigInteger(
                                "6901746346790563787434755862277025452451108972170386555162524223799295"));
        assertEquals(
                TypeEncoder.encodePacked(max232),
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero240 = new Uint240(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero240),
                "000000000000000000000000000000000000000000000000000000000000");
        Uint max240 =
                new Uint240(
                        new BigInteger(
                                "1766847064778384329583297500742918515827483896875618958121606201292619775"));
        assertEquals(
                TypeEncoder.encodePacked(max240),
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

        Uint zero248 = new Uint248(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero248),
                "00000000000000000000000000000000000000000000000000000000000000");
        Uint max248 =
                new Uint248(
                        new BigInteger(
                                "452312848583266388373324160190187140051835877600158453279131187530910662655"));
        assertEquals(
                TypeEncoder.encodePacked(max248),
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

    }

    @Test
    public void testIntEncodePacked() {
        Int zero8 = new Int8(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero8),
                ("00"));

        Int max8 = new Int8(BigInteger.valueOf(127));
        assertEquals(
                TypeEncoder.encodePacked(max8),
                ("7f"));

        Int min8 = new Int8(BigInteger.valueOf(-128));
        assertEquals(
                TypeEncoder.encodePacked(min8),
                ("80"));

        Int zero16 = new Int16(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero16),
                ("0000"));

        Int max16 = new Int16(BigInteger.valueOf(32767));
        assertEquals(
                TypeEncoder.encodePacked(max16),
                ("7fff"));

        Int min16 = new Int16(BigInteger.valueOf(-32768));
        assertEquals(
                TypeEncoder.encodePacked(min16),
                ("8000"));

        Int zero24 = new Int24(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero24),
                ("000000"));

        Int max24 = new Int24(BigInteger.valueOf(8388607));
        assertEquals(
                TypeEncoder.encodePacked(max24),
                ("7fffff"));

        Int min24 = new Int24(BigInteger.valueOf(-8388608));
        assertEquals(
                TypeEncoder.encodePacked(min24),
                ("800000"));

        Int zero32 = new Int32(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero32),
                ("00000000"));

        Int max32 = new Int32(BigInteger.valueOf(2147483647));
        assertEquals(
                TypeEncoder.encodePacked(max32),
                ("7fffffff"));

        Int min32 = new Int32(BigInteger.valueOf(-2147483648));
        assertEquals(
                TypeEncoder.encodePacked(min32),
                ("80000000"));

        Int zero40 = new Int40(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero40),
                ("0000000000"));

        Int max40 = new Int40(BigInteger.valueOf(549755813887L));
        assertEquals(
                TypeEncoder.encodePacked(max40),
                ("7fffffffff"));

        Int min40 = new Int40(BigInteger.valueOf(-549755813888L));
        assertEquals(
                TypeEncoder.encodePacked(min40),
                ("8000000000"));

        Int zero48 = new Int48(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodePacked(zero48),
                ("000000000000"));

        Int max48 = new Int48(BigInteger.valueOf(140737488355327L));
        assertEquals(
                TypeEncoder.encodePacked(max48),
                ("7fffffffffff"));

//        Int min48 = new Int48(BigInteger.valueOf(-140737488355328L));
//        assertEquals(
//                TypeEncoder.encodePacked(min48),
//                ("ffffffffffffffffffffffffffffffffffffffffffffffffffff800000000000"));
//
//        Int zero56 = new Int48(BigInteger.ZERO);
//        assertEquals(
//                TypeEncoder.encodePacked(zero56),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max56 = new Int56(BigInteger.valueOf(36028797018963967L));
//        assertEquals(
//                TypeEncoder.encodePacked(max56),
//                ("000000000000000000000000000000000000000000000000007fffffffffffff"));
//
//        Int min56 = new Int56(BigInteger.valueOf(-36028797018963968L));
//        assertEquals(
//                TypeEncoder.encodePacked(min56),
//                ("ffffffffffffffffffffffffffffffffffffffffffffffffff80000000000000"));
//
//        Int zero64 = new Int64(BigInteger.ZERO);
//        assertEquals(
//                TypeEncoder.encodePacked(zero64),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max64 = new Int64(BigInteger.valueOf(java.lang.Long.MAX_VALUE));
//        assertEquals(
//                TypeEncoder.encodePacked(max64),
//                ("0000000000000000000000000000000000000000000000007fffffffffffffff"));
//
//        Int min64 = new Int64(BigInteger.valueOf(java.lang.Long.MIN_VALUE));
//        assertEquals(
//                TypeEncoder.encodePacked(min64),
//                ("ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000"));
//
//        Int zero72 = new Int72(BigInteger.ZERO);
//        assertEquals(
//                TypeEncoder.encodePacked(zero72),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max72 = new Int72(new BigInteger("2361183241434822606847"));
//        assertEquals(
//                TypeEncoder.encodePacked(max72),
//                ("00000000000000000000000000000000000000000000007fffffffffffffffff"));
//
//        Int min72 = new Int72(new BigInteger("-2361183241434822606848"));
//        assertEquals(
//                TypeEncoder.encodePacked(min72),
//                ("ffffffffffffffffffffffffffffffffffffffffffffff800000000000000000"));
//
//        Int zero80 = new Int80(BigInteger.ZERO);
//        assertEquals(
//                TypeEncoder.encodePacked(zero80),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max80 = new Int80(new BigInteger("604462909807314587353087"));
//        assertEquals(
//                TypeEncoder.encodePacked(max80),
//                ("000000000000000000000000000000000000000000007fffffffffffffffffff"));
//
//        Int min80 = new Int80(new BigInteger("-604462909807314587353088"));
//        assertEquals(
//                TypeEncoder.encodePacked(min80),
//                ("ffffffffffffffffffffffffffffffffffffffffffff80000000000000000000"));
//
//        Int zero88 = new Int88(BigInteger.ZERO);
//        assertEquals(
//                TypeEncoder.encodePacked(zero88),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max88 = new Int88(new BigInteger("154742504910672534362390527"));
//        assertEquals(
//                TypeEncoder.encodePacked(max88),
//                ("0000000000000000000000000000000000000000007fffffffffffffffffffff"));
//
//        Int min88 = new Int88(new BigInteger("-154742504910672534362390528"));
//        assertEquals(
//                TypeEncoder.encodePacked(min88),
//                ("ffffffffffffffffffffffffffffffffffffffffff8000000000000000000000"));
//
//        Int zero96 = new Int96(BigInteger.ZERO);
//        assertEquals(
//                TypeEncoder.encodePacked(zero96),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max96 = new Int96(new BigInteger("39614081257132168796771975167"));
//        assertEquals(
//                TypeEncoder.encodePacked(max96),
//                ("00000000000000000000000000000000000000007fffffffffffffffffffffff"));
//
//        Int min96 = new Int96(new BigInteger("-39614081257132168796771975168"));
//        assertEquals(
//                TypeEncoder.encodePacked(min96),
//                ("ffffffffffffffffffffffffffffffffffffffff800000000000000000000000"));
//
//        Int zero104 = new Int104(BigInteger.ZERO);
//        assertEquals(
//                TypeEncoder.encodePacked(zero104),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max104 = new Int104(new BigInteger("10141204801825835211973625643007"));
//        assertEquals(
//                TypeEncoder.encodePacked(max104),
//                ("000000000000000000000000000000000000007fffffffffffffffffffffffff"));
//
//        Int min104 = new Int104(new BigInteger("-10141204801825835211973625643008"));
//        assertEquals(
//                TypeEncoder.encodePacked(min104),
//                ("ffffffffffffffffffffffffffffffffffffff80000000000000000000000000"));
//
//        Int zero112 = new Int112(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero112),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max112 = new Int112(new BigInteger("2596148429267413814265248164610047"));
//        assertEquals(
//                TypeEncoder.encodePacked(max112),
//                ("0000000000000000000000000000000000007fffffffffffffffffffffffffff"));
//
//        Int min112 = new Int112(new BigInteger("-2596148429267413814265248164610048"));
//        assertEquals(
//                TypeEncoder.encodePacked(min112),
//                ("ffffffffffffffffffffffffffffffffffff8000000000000000000000000000"));
//
//        Int zero120 = new Int120(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero120),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max120 = new Int120(new BigInteger("664613997892457936451903530140172287"));
//        assertEquals(
//                TypeEncoder.encodePacked(max120),
//                ("00000000000000000000000000000000007fffffffffffffffffffffffffffff"));
//
//        Int min120 = new Int120(new BigInteger("-664613997892457936451903530140172288"));
//        assertEquals(
//                TypeEncoder.encodePacked(min120),
//                ("ffffffffffffffffffffffffffffffffff800000000000000000000000000000"));
//
//        Int zero128 = new Int128(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero128),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max128 = new Int128(new BigInteger("170141183460469231731687303715884105727"));
//        assertEquals(
//                TypeEncoder.encodePacked(max128),
//                ("000000000000000000000000000000007fffffffffffffffffffffffffffffff"));
//
//        Int min128 = new Int128(new BigInteger("-170141183460469231731687303715884105728"));
//        assertEquals(
//                TypeEncoder.encodePacked(min128),
//                ("ffffffffffffffffffffffffffffffff80000000000000000000000000000000"));
//
//        Int zero136 = new Int136(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero136),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max136 = new Int136(new BigInteger("43556142965880123323311949751266331066367"));
//        assertEquals(
//                TypeEncoder.encodePacked(max136),
//                ("0000000000000000000000000000007fffffffffffffffffffffffffffffffff"));
//
//        Int min136 = new Int136(new BigInteger("-43556142965880123323311949751266331066368"));
//        assertEquals(
//                TypeEncoder.encodePacked(min136),
//                ("ffffffffffffffffffffffffffffff8000000000000000000000000000000000"));
//
//        Int zero144 = new Int144(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero144),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max144 = new Int144(new BigInteger("11150372599265311570767859136324180752990207"));
//        assertEquals(
//                TypeEncoder.encodePacked(max144),
//                ("00000000000000000000000000007fffffffffffffffffffffffffffffffffff"));
//
//        Int min144 = new Int144(new BigInteger("-11150372599265311570767859136324180752990208"));
//        assertEquals(
//                TypeEncoder.encodePacked(min144),
//                ("ffffffffffffffffffffffffffff800000000000000000000000000000000000"));
//
//        Int zero152 = new Int152(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero152),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max152 = new Int152(new BigInteger("2854495385411919762116571938898990272765493247"));
//        assertEquals(
//                TypeEncoder.encodePacked(max152),
//                ("000000000000000000000000007fffffffffffffffffffffffffffffffffffff"));
//
//        Int min152 = new Int152(new BigInteger("-2854495385411919762116571938898990272765493248"));
//        assertEquals(
//                TypeEncoder.encodePacked(min152),
//                ("ffffffffffffffffffffffffff80000000000000000000000000000000000000"));
//
//        Int zero160 = new Int160(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero160),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max160 = new Int160(new BigInteger("730750818665451459101842416358141509827966271487"));
//        assertEquals(
//                TypeEncoder.encodePacked(max160),
//                ("0000000000000000000000007fffffffffffffffffffffffffffffffffffffff"));
//
//        Int min160 =
//                new Int160(new BigInteger("-730750818665451459101842416358141509827966271488"));
//        assertEquals(
//                TypeEncoder.encodePacked(min160),
//                ("ffffffffffffffffffffffff8000000000000000000000000000000000000000"));
//
//        Int zero168 = new Int168(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero168),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max168 =
//                new Int168(new BigInteger("187072209578355573530071658587684226515959365500927"));
//        assertEquals(
//                TypeEncoder.encodePacked(max168),
//                ("00000000000000000000007fffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min168 =
//                new Int168(new BigInteger("-187072209578355573530071658587684226515959365500928"));
//        assertEquals(
//                TypeEncoder.encodePacked(min168),
//                ("ffffffffffffffffffffff800000000000000000000000000000000000000000"));
//
//        Int zero176 = new Int176(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero176),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max176 =
//                new Int176(new BigInteger("47890485652059026823698344598447161988085597568237567"));
//        assertEquals(
//                TypeEncoder.encodePacked(max176),
//                ("000000000000000000007fffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min176 =
//                new Int176(
//                        new BigInteger("-47890485652059026823698344598447161988085597568237568"));
//        assertEquals(
//                TypeEncoder.encodePacked(min176),
//                ("ffffffffffffffffffff80000000000000000000000000000000000000000000"));
//
//        Int zero184 = new Int184(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero184),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max184 =
//                new Int184(
//                        new BigInteger("12259964326927110866866776217202473468949912977468817407"));
//        assertEquals(
//                TypeEncoder.encodePacked(max184),
//                ("0000000000000000007fffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min184 =
//                new Int184(
//                        new BigInteger(
//                                "-12259964326927110866866776217202473468949912977468817408"));
//        assertEquals(
//                TypeEncoder.encodePacked(min184),
//                ("ffffffffffffffffff8000000000000000000000000000000000000000000000"));
//
//        Int zero192 = new Int192(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero192),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max192 =
//                new Int192(
//                        new BigInteger(
//                                "3138550867693340381917894711603833208051177722232017256447"));
//        assertEquals(
//                TypeEncoder.encodePacked(max192),
//                ("00000000000000007fffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min192 =
//                new Int192(
//                        new BigInteger(
//                                "-3138550867693340381917894711603833208051177722232017256448"));
//        assertEquals(
//                TypeEncoder.encodePacked(min192),
//                ("ffffffffffffffff800000000000000000000000000000000000000000000000"));
//
//        Int zero200 = new Int200(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero200),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max200 =
//                new Int200(
//                        new BigInteger(
//                                "803469022129495137770981046170581301261101496891396417650687"));
//        assertEquals(
//                TypeEncoder.encodePacked(max200),
//                ("000000000000007fffffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min200 =
//                new Int200(
//                        new BigInteger(
//                                "-803469022129495137770981046170581301261101496891396417650688"));
//        assertEquals(
//                TypeEncoder.encodePacked(min200),
//                ("ffffffffffffff80000000000000000000000000000000000000000000000000"));
//
//        Int zero208 = new Int208(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero208),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max208 =
//                new Int208(
//                        new BigInteger(
//                                "205688069665150755269371147819668813122841983204197482918576127"));
//        assertEquals(
//                TypeEncoder.encodePacked(max208),
//                ("0000000000007fffffffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min208 =
//                new Int208(
//                        new BigInteger(
//                                "-205688069665150755269371147819668813122841983204197482918576128"));
//        assertEquals(
//                TypeEncoder.encodePacked(min208),
//                ("ffffffffffff8000000000000000000000000000000000000000000000000000"));
//
//        Int zero216 = new Int216(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero216),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max216 =
//                new Int216(
//                        new BigInteger(
//                                "52656145834278593348959013841835216159447547700274555627155488767"));
//        assertEquals(
//                TypeEncoder.encodePacked(max216),
//                ("00000000007fffffffffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min216 =
//                new Int216(
//                        new BigInteger(
//                                "-52656145834278593348959013841835216159447547700274555627155488768"));
//        assertEquals(
//                TypeEncoder.encodePacked(min216),
//                ("ffffffffff800000000000000000000000000000000000000000000000000000"));
//
//        Int zero224 = new Int224(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero224),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max224 =
//                new Int224(
//                        new BigInteger(
//                                "13479973333575319897333507543509815336818572211270286240551805124607"));
//        assertEquals(
//                TypeEncoder.encodePacked(max224),
//                ("000000007fffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min224 =
//                new Int224(
//                        new BigInteger(
//                                "-13479973333575319897333507543509815336818572211270286240551805124608"));
//        assertEquals(
//                TypeEncoder.encodePacked(min224),
//                ("ffffffff80000000000000000000000000000000000000000000000000000000"));
//
//        Int zero232 = new Int232(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero232),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max232 =
//                new Int232(
//                        new BigInteger(
//                                "3450873173395281893717377931138512726225554486085193277581262111899647"));
//        assertEquals(
//                TypeEncoder.encodePacked(max232),
//                ("0000007fffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min232 =
//                new Int232(
//                        new BigInteger(
//                                "-3450873173395281893717377931138512726225554486085193277581262111899648"));
//        assertEquals(
//                TypeEncoder.encodePacked(min232),
//                ("ffffff8000000000000000000000000000000000000000000000000000000000"));
//
//        Int zero240 = new Int240(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero240),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max240 =
//                new Int240(
//                        new BigInteger(
//                                "883423532389192164791648750371459257913741948437809479060803100646309887"));
//        assertEquals(
//                TypeEncoder.encodePacked(max240),
//                ("00007fffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min240 =
//                new Int240(
//                        new BigInteger(
//                                "-883423532389192164791648750371459257913741948437809479060803100646309888"));
//        assertEquals(
//                TypeEncoder.encodePacked(min240),
//                ("ffff800000000000000000000000000000000000000000000000000000000000"));
//
//        Int zero248 = new Int248(BigInteger.ZERO);
//
//        assertEquals(
//                TypeEncoder.encodePacked(zero248),
//                ("0000000000000000000000000000000000000000000000000000000000000000"));
//
//        Int max248 =
//                new Int248(
//                        new BigInteger(
//                                "226156424291633194186662080095093570025917938800079226639565593765455331327"));
//        assertEquals(
//                TypeEncoder.encodePacked(max248),
//                ("007fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
//
//        Int min248 =
//                new Int248(
//                        new BigInteger(
//                                "-226156424291633194186662080095093570025917938800079226639565593765455331328"));
//        assertEquals(
//                TypeEncoder.encodePacked(min248),
//                ("ff80000000000000000000000000000000000000000000000000000000000000"));
//        Int minusOne = new Int(BigInteger.valueOf(-1));
//        assertEquals(
//                TypeEncoder.encodePacked(minusOne),
//                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }

    @Test
    public void testStaticBytesEncodePacked() {
        Bytes staticBytes = new Bytes6(new byte[] {0, 1, 2, 3, 4, 5});
        assertEquals(
                TypeEncoder.encodePacked(staticBytes),
                ("000102030405"));

        Bytes empty = new Bytes1(new byte[] {0});
        assertEquals(
                TypeEncoder.encodePacked(empty),
                ("00"));

        Bytes ones = new Bytes1(new byte[] {127});
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
}