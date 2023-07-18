package com.zml.acl.controller;

import com.zml.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 16:44
 */
@Api(tags = "登录接口")
@RestController
//@CrossOrigin
@RequestMapping("/admin/acl/index")
public class IndexController {

    //    登录接口
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login() {
        HashMap<String, Object> map = new HashMap<>(10);
        map.put("token", "admin-token");
        return Result.ok(map);
    }

    //    获取用户信息接口
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result getInfo() {
        Map<String, Object> map = new HashMap<>(10);
        map.put("name", "zhangminlei");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }

    //    退出接口
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result logout() {
        return Result.ok(null);
    }
}
