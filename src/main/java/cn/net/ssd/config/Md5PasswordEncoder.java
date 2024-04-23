package cn.net.ssd.config;

import cn.net.ssd.common.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2024/4/7 12:18
 */
public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.MD5(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String encodedRaw = encode(rawPassword);
        return encodedRaw.equals(encodedPassword);
    }
}
