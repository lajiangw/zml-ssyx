package com.zml.home.controller;

import com.zml.auth.AuthContextHolder;
import com.zml.home.service.ItemService;
import com.zml.result.Result;
import com.zml.utils.JwtHelper;
import io.swagger.annotations.Api;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 17:09
 */
@Api(tags = "商品详情")
@RestController
@RequestMapping("/home")
public class ItemApiController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/item/{id}")
    public Result index(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
//        Long userId = AuthContextHolder.getUserId();
        Map<String, Object> map = itemService.item(id, userId);
        return Result.ok(map);
    }

}
