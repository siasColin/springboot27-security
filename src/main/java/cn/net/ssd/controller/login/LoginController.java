package cn.net.ssd.controller.login;

import cn.net.ssd.common.component.RedisUtil;
import cn.net.ssd.common.util.JwtUtils;
import cn.net.ssd.common.util.SpringSecurityUtil;
import cn.net.ssd.common.util.StringUtil;
import cn.net.ssd.config.RsaKeyProperties;
import cn.net.ssd.model.common.ResultCode;
import cn.net.ssd.model.common.ResultInfo;
import cn.net.ssd.model.sysManage.SysUser;
import cn.net.ssd.service.sysManage.ISysUserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ssd on 2020-2-27.
 */
@Controller
@Api(tags = "登录认证相关接口")
@ApiSupport(author = "colin", order = 1)
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private RsaKeyProperties prop;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/loginerror")
    public String loginerror(HttpServletRequest request, Map<String, Object> modelMap) {
        AuthenticationException authenticationException =
                (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (authenticationException instanceof UsernameNotFoundException || authenticationException instanceof BadCredentialsException) {
            modelMap.put("msg", "用户名或密码错误");
        } else if (authenticationException instanceof DisabledException) {
            modelMap.put("msg", "用户已被禁用");
        } else if (authenticationException instanceof LockedException) {
            modelMap.put("msg", "账户被锁定");
        } else if (authenticationException instanceof AccountExpiredException) {
            modelMap.put("msg", "账户过期");
        } else if (authenticationException instanceof CredentialsExpiredException) {
            modelMap.put("msg", "证书过期");
        } else {
            modelMap.put("msg", "登录失败");
        }
        return "login";
    }

    @RequestMapping("/loginSuccess")
    public String loginSuccess() {
        return "redirect:main";
    }

    @RequestMapping("/tologin")
    public String tologin(Map<String, Object> modelMap) {
        return "login";
    }

    /**
     * 登录成功后跳转首页
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/main")
    public String main(Map<String, Object> modelMap) {
        SysUser sysUser = SpringSecurityUtil.getPrincipal();
        SimpleDateFormat fds = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String time = fds.format(new Date());
        modelMap.put("time", time);
        modelMap.put("sysUser", sysUser);
        return "index";
    }

    @GetMapping("/loginToken")
    @ResponseBody
    public ResultInfo getLoginToken() {
        SysUser loginUser = SpringSecurityUtil.getPrincipal();
        if (loginUser != null) {
            SysUser tokenUser = new SysUser();
            tokenUser.setLoginName(loginUser.getLoginName());
            //设置过期时间10s
            String token = JwtUtils.generateTokenExpireInSeconds(loginUser, prop.getPrivateKey(), 10);
            return ResultInfo.builder().resultCode(ResultCode.SUCCESS).data("Bearer " + token).build();
        } else {
            return ResultInfo.builder().resultCode(ResultCode.STATUS_CODE_406).build();
        }
    }

    /**
     * 处理session超时
     *
     * @param request
     * @return
     */
    @RequestMapping("/sessionInvalid")
    @ResponseBody
    public ResultInfo handleError(HttpServletRequest request) {
        ResultInfo result = ResultInfo.builder().resultCode(ResultCode.SESSIONINVALID).build();
        return result;
    }

    /**
     * 处理session超时
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/sessionInvalid", produces = "text/html")
    public String handleError(HttpServletRequest request, Map<String, Object> modelMap) {
        modelMap.put("msg", "登录状态已过期，请重新登录！");
        return "login";
    }

    @PostMapping("/login/token")
    @ResponseBody
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取token", notes = "根据用户名密码获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", required = true)
    })
    public ResultInfo loginToken(String username, String password, HttpServletRequest request) {
        if (StringUtil.checkNull(username) || StringUtil.checkNull(password)) {
            return ResultInfo.builder().resultCode(ResultCode.LOGIN_ERROR).build();
        } else {
            UserDetails userDetails = userService.loadUserByUsername(username);
            //密码需要客户端加密后传递
            if (!password.equals(userDetails.getPassword())) {
                return ResultInfo.builder().resultCode(ResultCode.LOGIN_ERROR).build();
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SysUser user = new SysUser();
            //添加需要放入token中的信息，登录名必须，其他按需添加
            user.setLoginName(userDetails.getUsername());
            //生成token，有效期10分钟
            String token = JwtUtils.generateTokenExpireInMinutes(user, prop.getPrivateKey(), 10);
            //再生成一个refresh_token，用于刷新token,有效期2小时
            String refresh_token = JwtUtils.generateTokenExpireInMinutes(user, prop.getPrivateKey(), 2 * 60 + 1);
            redisUtil.set("Refresh_token:" + token, refresh_token, 2 * 60 * 60);
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", "Bearer " + token);
            tokenMap.put("tokenHead", "Authorization");
            return ResultInfo.builder().resultCode(ResultCode.SUCCESS).data(tokenMap).build();
        }
    }
}
