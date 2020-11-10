package io.ztc.tools;

public class DataUtils {
    /**
     * byte[]转HEX字符串
     * @param buffer 转换数组
     * @return 目标字符串
     */
    public static String bytesToHex(byte [] buffer){
        StringBuilder h = new StringBuilder();

        for (byte b : buffer) {
            String temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h.append(" ").append(temp);
        }

        return h.toString();

    }

    /**
     * HEX字符串转byte[]
     * @param hexString HEX字符串
     * @return byte[]
     */
    public static byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.trim().replaceAll("\\s*", ""); // 去除字符串中的空格

        String hexFormat = "0123456789ABCDEF";

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (hexFormat.indexOf(hexChars[pos]) << 4 | hexFormat
                    .indexOf(hexChars[pos + 1]));
        }
        return d;
    }
}
