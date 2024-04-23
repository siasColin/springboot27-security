package cn.net.ssd.controller.common;

import cn.net.ssd.model.common.ResultCode;
import cn.net.ssd.model.common.ResultInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2024/4/8 8:31
 */
@Controller
@RequestMapping("/common")
@Api(tags = "公共接口")
@ApiSupport(author = "colin", order = 2)
public class CommonController {

    /**
     * 测试接口
     *
     * @return ResultInfo 返回操作的结果信息，包括结果代码和相关消息。
     */
    @PostMapping("test")
    @ResponseBody
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "测试接口", notes = "测试接口")
    public ResultInfo test() {
        // 构建并返回一个表示操作成功的结果信息
        return ResultInfo.builder().resultCode(ResultCode.SUCCESS).build();
    }
}
