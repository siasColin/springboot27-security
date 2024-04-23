package cn.net.ssd.common.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2023/3/6 13:45
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String moduleName = "退出系统";

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {
        response.sendRedirect(request.getContextPath() + "/tologin");
    }
}
