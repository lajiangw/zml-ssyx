package com.zml.home.controller;

import com.zml.auth.AuthContextHolder;
import com.zml.home.service.HomeService;
import com.zml.result.Result;
import com.zml.utils.JwtHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 17:05
 */
@Api(tags = "首页")
@RestController
@RequestMapping("/home")
public class HomeApiController {

    @Resource
    private HomeService homeService;

    @GetMapping("/index")
    @ApiOperation("首页数据显示接口")
    public Result index(HttpServletRequest request) {
        System.out.println(123);
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        return Result.ok(homeService.homeDate(userId));
    }
}
