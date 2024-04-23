package cn.net.ssd.common.util;

import java.security.MessageDigest;

/**
 * MD5加密算法
 * 项目名称：湖南突发
 * 类名称：MD5Util
 * 类描述：
 * 创建人：孙江
 * 创建时间：2015年7月16日 上午9:25:35
 * 修改人：孙江
 * 修改时间：2015年7月16日 上午9:25:35
 * 修改备注：
 */
public class MD5Util {
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象  
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要  
            mdInst.update(btInput);
            //获得密文  
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式  
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5Java(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;
    }
}
