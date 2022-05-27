package com.pro.maluli.common.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class StringUtils {

    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] stringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;
            char hex_char1 = hexString.charAt(i);
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }

    /**
     * 比较大小
     *
     * @param v1
     * @param v2
     * @return -1：v1<v2 0：v1=v2 1:v1>v2
     */
    public static int compareTo(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        return b1.compareTo(b2);

    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2String(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase(Locale.CHINA);
    }

    /**
     * 二进制转化成16进制
     *
     * @param b
     * @return
     */
    public static byte[] byte2hex(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 生成随机数字和字母,
     *
     * @param
     * @return
     * @author Guoyh
     * @date 2018/10/10 12:25
     */
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 8-16位数字字母混合,不能全为数字,不能全为字母,首位不能为数字
     *
     * @param pass
     * @return
     */
    public static boolean validatePhonePass(String pass) {
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return !TextUtils.isEmpty(pass) && pass.matches(passRegex);
    }

    /**
     * String 转long
     *
     * @param o
     * @return
     */
    public final static long String2Long(String o) {
        if (o != null) {
            try {
                return Long.valueOf(o);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }

    }

    /**
     * 判断为空
     *
     * @param o
     * @return
     */
    public static String StringToNull(String o) {
        return TextUtils.isEmpty(o) ? "" : o;

    }

    public static String sortImg(List<String> strings) {
        if (strings == null || strings.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strings.size(); i++) {
            if (i == strings.size() - 1) {
                stringBuffer.append(strings.get(i));
            } else {
                stringBuffer.append(strings.get(i) + ",");
            }

        }
        return stringBuffer.toString();
    }
}
