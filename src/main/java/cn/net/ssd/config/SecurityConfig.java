package cn.net.ssd.config;

import cn.net.ssd.common.component.MyAccessDeniedHandler;
import cn.net.ssd.common.component.MyAuthenticationEntryPoint;
import cn.net.ssd.common.component.MyLogoutSuccessHandler;
import cn.net.ssd.filter.JwtVerifyFilter;
import cn.net.ssd.service.sysManage.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Package: cn.net.ssd.common.config
 * @Author: sxf
 * @Date: 2020-3-4
 * @Description: 安全配置
 * 开启方法注解支持，我们设置prePostEnabled = true，支持spring表达式注解 ，是为了后面能够使用hasRole()这类表达式
 * <p>
 * securedEnabled，SpringSecurity提供的注解，@Secured({"ROLE_ADMIN","ROLE_PRODUCT"})//SpringSecurity注解
 * 基于SpringBoot的spring-boot-starter-security
 * 我们可以看到自动配置类中导入了WebSecurityEnablerConfiguration，
 * 而WebSecurityEnablerConfiguration上已经加了 @EnableWebSecurity注解
 * 所以其实在这里我们是可以不用自己添加的
 * @Import({SpringBootWebSecurityConfiguration.class, WebSecurityEnablerConfiguration.class, SecurityDataConfiguration.class})
 * public class SecurityAutoConfiguration {
 * @EnableWebSecurity public class WebSecurityEnablerConfiguration {
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    //需要和生成TokenBasedRememberMeServices的密钥相同
    private static final String KEY = "ssd";
    @Autowired
    private RsaKeyProperties prop;
    @Autowired
    private AntMatchersProperties antMatchers;

    @Autowired
    private MyLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private ISysUserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.loadUserByUsername(username);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtVerifyFilter jwtVerifyFilter() {
        return new JwtVerifyFilter();
    }

    @Bean
    public AccessDeniedHandler myAccessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public MyAuthenticationEntryPoint myAuthenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 用于：自定义SpringSecurity配置信息
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
                .authorizeRequests();
        //不需要保护的资源路径允许访问
        registry.antMatchers(antMatchers.getPermitUrls()).permitAll();
        registry.antMatchers(antMatchers.getCheckTokenUrls()).permitAll();
        //允许跨域请求的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS)
                .permitAll();
        registry.and().csrf().ignoringAntMatchers(antMatchers.getPermitUrls()).ignoringAntMatchers(antMatchers.getCheckTokenUrls());
        registry.and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                //允许iframe加载同源的资源
//                .headers().frameOptions().sameOrigin()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                //所有的请求需要认证即登陆后才能访问
                .anyRequest().authenticated()
                .and()
                //form表单验证
                .formLogin().loginPage("/tologin").loginProcessingUrl("/login")
                .permitAll()
                //设置默认登陆成功跳转的页面
                .defaultSuccessUrl("/loginSuccess")
                //登陆失败的请求
                .failureUrl("/loginerror")
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .sessionManagement()
                .invalidSessionUrl("/sessionInvalid")
                .sessionAuthenticationErrorUrl("/sessionInvalid")
                //处理没有访问权限情况
                .and().exceptionHandling().accessDeniedHandler(myAccessDeniedHandler()).authenticationEntryPoint(myAuthenticationEntryPoint())
                .and()
                .addFilterBefore(jwtVerifyFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * 完全绕过了spring security的所有filter，相当于不走spring security
     * 主要用于：让静态资源不走springsecurity的过滤器
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().antMatchers(antMatchers.getIgnoreUrls());
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
