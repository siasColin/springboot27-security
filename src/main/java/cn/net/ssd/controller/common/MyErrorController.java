package cn.net.ssd.controller.common;

import cn.net.ssd.model.common.ResultCode;
import cn.net.ssd.model.common.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认的/error处理会在springboot中的ErrorMvcAutoConfiguration>>BasicErrorController中进行
 * 这里我们用自定义的
 * Created by ssd on 2020-2-27.
 */
@Controller
public class MyErrorController implements ErrorController {
    Logger Logger = LoggerFactory.getLogger(MyErrorController.class);

    public String getErrorPath() {
        return "/error";
    }

    /**
     * 处理页面请求异常
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/error", produces = "text/html")
    public String handleErrorHtml(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        ResultInfo result = null;
        ResultInfo.ResultInfoBuilder builder = ResultInfo.builder();
        if (statusCode == 400) {
            builder.resultCode(ResultCode.STATUS_CODE_400);
        } else if (statusCode == 403) {
            builder.resultCode(ResultCode.STATUS_CODE_403);
        } else if (statusCode == 404) {
            builder.resultCode(ResultCode.STATUS_CODE_404);
        } else if (statusCode == 405) {
            builder.resultCode(ResultCode.STATUS_CODE_405);
        } else if (statusCode == 500) {
            builder.resultCode(ResultCode.STATUS_CODE_500);
        } else {
            result = request.getAttribute("ext") == null ? builder.resultCode(ResultCode.UNKNOWN_ERROR).build() :
                    (ResultInfo) request.getAttribute("ext");
        }
        if(result == null){
            result = builder.build();
        }
        request.setAttribute("code", result.getCode());
        request.setAttribute("msg", result.getMsg());
        if (result.getTotal() != null) {
            request.setAttribute("total", result.getTotal());
        }
        if (result.getData() != null) {
            request.setAttribute("data", result.getData());
        }
        return "error/error";
    }

    /**
     * 处理非页面异常
     *
     * @param request
     * @return
     */
    @RequestMapping("/error")
    @ResponseBody
    public ResultInfo handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        ResultInfo result = null;
        ResultInfo.ResultInfoBuilder builder = ResultInfo.builder();
        if (statusCode == 400) {
            builder.resultCode(ResultCode.STATUS_CODE_400);
        } else if (statusCode == 403) {
            builder.resultCode(ResultCode.STATUS_CODE_403);
        } else if (statusCode == 404) {
            builder.resultCode(ResultCode.STATUS_CODE_404);
        } else if (statusCode == 405) {
            builder.resultCode(ResultCode.STATUS_CODE_405);
        } else if (statusCode == 500) {
            builder.resultCode(ResultCode.STATUS_CODE_500);
        } else {
            result = request.getAttribute("ext") == null ? builder.resultCode(ResultCode.UNKNOWN_ERROR).build() :
                    (ResultInfo) request.getAttribute("ext");
        }
        if(result == null){
            result = builder.build();
        }
        return result;
    }

    @RequestMapping(value = "/authException", produces = "text/html")
    public String authExceptionHtml(HttpServletRequest request) {
        ResultInfo resultInfo = ResultInfo.builder().resultCode(ResultCode.STATUS_CODE_403).build();
        request.setAttribute("code", resultInfo.getCode());
        request.setAttribute("msg", resultInfo.getMsg());
        return "error/authException";
    }

    @RequestMapping("/authException")
    @ResponseBody
    public ResultInfo authException(HttpServletRequest request) {
        return ResultInfo.builder().resultCode(ResultCode.STATUS_CODE_403).build();
    }

}
