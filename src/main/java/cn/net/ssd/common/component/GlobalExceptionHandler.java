package cn.net.ssd.common.component;

import cn.net.ssd.common.exception.BusinessRuntimeException;
import cn.net.ssd.model.common.ResultCode;
import cn.net.ssd.model.common.ResultInfo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        ResultInfo.ResultInfoBuilder resultBuilder = ResultInfo.builder().resultCode(ResultCode.UNKNOWN_ERROR);
        if (e instanceof BusinessRuntimeException) {
            BusinessRuntimeException bex = (BusinessRuntimeException) e;
            resultBuilder.resultCode(bex.getResultCode());
        } else {
            if (e instanceof AccessDeniedException) {//没有权限
                resultBuilder.resultCode(ResultCode.STATUS_CODE_406);
            } else {
                if (e instanceof InvocationTargetException && e.getMessage() == null) {
                    resultBuilder.msg(((InvocationTargetException) e).getTargetException().getMessage());
                } else {
                    resultBuilder.msg(e.toString());
                }
            }
        }
        // tomcat 处理
        /*if (e instanceof ClientAbortException) {//java.io.IOException: 你的主机中的软件中止了一个已建立的连接。
            return null;
        }*/
        ResultInfo result = resultBuilder.build();
        // undertow 处理
        if (result.getMsg() != null
                && (result.getMsg().trim().contains("java.io.IOException: UT010029: Stream is closed") || result.getMsg().trim().contains("java.io.IOException: 你的主机中的软件中止了一个已建立的连接。"))) {
            return null;
        }
        Object ext = request.getAttribute("ext");
        if (ext != null) {
            return null;
        }
        request.setAttribute("ext", result);
        return "forward:/error";
    }
}
