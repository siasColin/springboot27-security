package cn.net.ssd.filter;

import cn.net.ssd.common.component.RedisUtil;
import cn.net.ssd.common.util.JsonUtils;
import cn.net.ssd.common.util.JwtUtils;
import cn.net.ssd.config.AntMatchersProperties;
import cn.net.ssd.config.RsaKeyProperties;
import cn.net.ssd.model.common.Payload;
import cn.net.ssd.model.common.ResultCode;
import cn.net.ssd.model.common.ResultInfo;
import cn.net.ssd.model.sysManage.SysUser;
import cn.net.ssd.service.sysManage.ISysUserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Package: cn.net.ssd.filter
 * @Author: sxf
 * @Date: 2020-3-6
 * @Description:
 */
public class JwtVerifyFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtVerifyFilter.class);
    @Autowired
    private ISysUserService userService;
    @Autowired
    private RsaKeyProperties prop;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AntMatchersProperties antMatchers;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        boolean doFlag = false;
        for (String oneallowUrl : antMatchers.getCheckTokenUrls()) {//判断是否拦截
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(oneallowUrl);
            if (matcher.matches(request)) {//匹配上，忽略拦截
                doFlag = true;
                break;
            }
        }
        if (doFlag) {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                //如果携带错误的token，则给用户提示请登录！
                response(response, ResultCode.TOKEN_NOTFOUND);
            } else {
                String token = header.replace("Bearer ", "");
                //验证tken是否正确
                try {
                    Payload<SysUser> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), SysUser.class);
                    SysUser user = payload.getUserInfo();
                    if (user != null) {//验证通过
                        UserDetails userDetails = userService.loadUserByUsername(user.getLoginName());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        chain.doFilter(request, response);
                    } else {
                        response(response, ResultCode.TOKEN_ERROR);
                    }
                } catch (ExpiredJwtException e) {//token过期
                    //判断用于刷新的token是否过期
                    // 从redis中取Refresh_token
                    Object refresh_token_obj = redisUtil.get("Refresh_token:" + token);
                    String refresh_token = refresh_token_obj == null ? null : refresh_token_obj.toString();
                    try {
                        if (refresh_token != null && !refresh_token.trim().equals("")) { // refresh_token 已过期
                            Payload<SysUser> payload = JwtUtils.getInfoFromToken(refresh_token, prop.getPublicKey(), SysUser.class);
                            SysUser user = payload.getUserInfo();
                            if (user != null) {//验证通过
                                String loginName = user.getLoginName();
                                UserDetails userDetails = userService.loadUserByUsername(loginName);
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                //生成新的token
                                String newToken = JwtUtils.generateTokenExpireInMinutes(user, prop.getPrivateKey(), 10);
                                //再生成一个refresh_token，用于刷新token,有效期2小时
                                String newRefresh_token = JwtUtils.generateTokenExpireInMinutes(user, prop.getPrivateKey(), 2 * 60 + 1);
                                //将两个Token写入response头信息中
                                response.addHeader("Authorization", "Bearer " + newToken);
                                // Refresh_token 保存在服务端
                                redisUtil.set("Refresh_token:" + newToken, newRefresh_token, 2 * 60 * 60);
                                redisUtil.del("Refresh_token:" + token);
                                chain.doFilter(request, response);
                            } else {
                                response(response, ResultCode.TOKEN_ERROR);
                            }
                        } else {
                            response(response, ResultCode.TOKEN_ERROR);
                        }
                    } catch (ExpiredJwtException ex) {//refresh_token过期
                        response(response, ResultCode.TOKEN_ERROR);
                    } catch (Exception exc) {
                        response(response, ResultCode.TOKEN_ERROR);
                    }
                } catch (Exception e) {
                    response(response, ResultCode.TOKEN_ERROR);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 给客户端返回提示数据
     *
     * @param response
     * @param resultCode
     */
    private void response(HttpServletResponse response, ResultCode resultCode) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            out = response.getWriter();
            ResultInfo resultInfo = ResultInfo.builder().resultCode(resultCode).build();
            out.write(JsonUtils.toString(resultInfo));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
        }

    }
}
