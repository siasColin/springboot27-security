package cn.net.ssd.controller.sysManage;

import cn.net.ssd.model.common.ResultCode;
import cn.net.ssd.model.common.ResultInfo;
import cn.net.ssd.model.sysManage.SysUser;
import cn.net.ssd.service.sysManage.ISysUserService;
import com.github.pagehelper.PageInfo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Package: cn.net.colin.controller.sysManage
 * @Author: sxf
 * @Date: 2020-3-29
 * @Description: 用户管理
 */
@Controller
@RequestMapping("/userManage")
@Api(tags = "用户管理")
@ApiSupport(author = "colin", order = 3)
public class UserManageController {
    Logger logger = LoggerFactory.getLogger(UserManageController.class);

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 返回用户信息列表
     *
     * @param paramMap userName 用户名字（模糊查询）
     *                 orgCode 机构编码
     *                 roleIdNotBind 角色id，如果传入该字段则查询未绑定该角色的用户集合
     * @return ResultInfo 自定义结果返回实体类
     * @throws IOException
     */
    @PreAuthorize("hasAnyAuthority('ADMIN_AUTH','INSERT_AUTH')")
    @GetMapping("/userList")
    @ResponseBody
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取用户信息列表", notes = "返回用户信息列表信息",
            consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页记录数", required = true, example = "10", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名字", required = false, paramType = "query"),
            @ApiImplicitParam(name = "orgCode", value = "机构编码", required = false, paramType = "query"),
            @ApiImplicitParam(name = "roleIdNotBind", value = "角色id（查询未绑定该角色的用户集合）", required = false, example = "1", paramType = "query", dataType = "long")
    })
    public ResultInfo userList(@ApiIgnore @RequestParam Map<String, Object> paramMap) throws IOException {
        List<SysUser> userList = sysUserService.selectByParams(paramMap);
        PageInfo<SysUser> result = new PageInfo(userList);
        return ResultInfo.builder().resultCode(ResultCode.SUCCESS).data(userList).total(result.getTotal()).build();
    }

}
