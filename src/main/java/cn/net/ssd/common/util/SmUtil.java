package cn.net.ssd.common.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO 国密算法工具
 * @date 2023/5/23 15:52
 */
public class SmUtil {
    private static final SM3 sm3 = SM3.create();
    private static final Mode mode = Mode.ECB;
    private static final Padding padding = Padding.ZeroPadding;

    public static String encryptSM4(String data, String appsecret) {
        SM4 sm4 = new SM4(Mode.ECB, Padding.ZeroPadding, deriveSigningKey(appsecret));
        return sm4.encryptHex(data, CharsetUtil.CHARSET_UTF_8);
    }

    public static String decryptSM4(String data, String appsecret) {
        SM4 sm4 = new SM4(Mode.ECB, Padding.ZeroPadding, deriveSigningKey(appsecret));
        return sm4.decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encryptSM3(String data) {
        SM3 sm3 = SM3.create();
        return sm3.digestHex(data);
    }

    private static byte[] deriveSigningKey(String secret) {
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        String appsecret = "VNRRxH^LPXCh1111";
        String data = "这是测试数据aaa";
        System.out.println(SmUtil.encryptSM3(data));
        String s = SmUtil.encryptSM4(data, appsecret);
        System.out.println(s);
        System.out.println(SmUtil.decryptSM4(s, appsecret));
    }
}
