package org.web3j.protobuf;

public class ConvertStrByte {
    /**
     * Title:bytes2HexString
     * Description:字节数组转16进制字符串
     * param b
     *            字节数组
     * return 16进制字符串
     * throws
     */
    public static String bytesToHexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex);
        }
        return result.toString();
    }

    /**
     * Title:hexStringToBytes
     * Description:16进制字符串转字节数组
     * param src
     *            16进制字符串
     * return 字节数组
     * throws
     */
    public static byte[] hexStringToBytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * Title:string2HexString
     * Description:字符串转16进制字符串
     * param strPart
     *            字符串
     * return 16进制字符串
     * throws
     */
    public static String stringToHexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * Title:hexString2String
     * Description:16进制字符串转字符串
     * param src
     *            16进制字符串
     * return 字节数组
     * throws
     */
    public static String hexStringToString(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }

    /**
     * Title:char2Byte
     * Description:字符转成字节数据char-integer-byte
     * param src
     * return
     * throws
     */
    public static Byte charToByte(Character src) {
        return Integer.valueOf((int)src).byteValue();
    }

    /**
     * Title:intToHexString
     * Description:10进制数字转成16进制
     * param a 转化数据
     * param len 占用字节数
     * return
     * throws
     */
    private static String intToHexString(int a,int len){
        len<<=1;
        String hexString = Integer.toHexString(a);
        int b = len -hexString.length();
        if(b>0){
            for(int i=0;i<b;i++)  {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }
}
