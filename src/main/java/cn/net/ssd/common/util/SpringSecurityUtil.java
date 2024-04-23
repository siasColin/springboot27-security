package cn.net.ssd.common.util;

import cn.net.ssd.model.sysManage.SysUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * @Package: cn.net.ssd.common.util
 * @Author: sxf
 * @Date: 2020-3-14
 * @Description: springsecurity 工具类
 */
public class SpringSecurityUtil {

    /**
     * 获取SecurityContext中存放的用户信息
     *
     * @return
     */
    public static SysUser getPrincipal() {
        try {
            return (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param roles
     * @return
     */
    public static boolean hasRole(String... roles) {
        try {
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            Set<String> roleSet = AuthorityUtils.authorityListToSet(authorities);
            for (String role : roles) {
                if (roleSet.contains(role)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将用户信息存入SecurityContext中
     * 可用于用户信息的更新
     *
     * @param userDetails
     */
    public static void setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
