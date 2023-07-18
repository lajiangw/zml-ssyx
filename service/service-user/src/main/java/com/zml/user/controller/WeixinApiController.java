package com.zml.user.controller;

import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSONObject;
import com.zml.auth.AuthContextHolder;
import com.zml.constant.RedisConst;
import com.zml.exception.SsyxException;
import com.zml.result.Result;
import com.zml.result.ResultCodeEnum;
import com.zml.ssyx.enums.UserType;
import com.zml.ssyx.model.user.User;
import com.zml.ssyx.vo.user.LeaderAddressVo;
import com.zml.ssyx.vo.user.UserLoginVo;
import com.zml.user.service.UserService;
import com.zml.user.utils.ConstantPropertiesUtil;
import com.zml.user.utils.HttpClientUtils;
import com.zml.utils.JwtHelper;
import io.swagger.annotations.ApiOperation;
import jodd.time.TimeUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 11:39
 */

@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {


    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    //用户授权登录
    @ApiOperation(value = "微信登录获取openid(小程序)")
    @GetMapping("/wxLogin/{code}")
    public Result loginWx(@PathVariable String code) {
        //1 获取code 向微信固定登录接口发送请求
        System.out.println("回调接口调用");
        String wxOpenAppId = ConstantPropertiesUtil.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantPropertiesUtil.WX_OPEN_APP_SECRET;

        StringBuffer url = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");
        String tokenURl = String.format(url.toString(), wxOpenAppId, wxOpenAppSecret, code);
        String result = null;
        try {
            result = HttpClientUtils.get(tokenURl);
        } catch (Exception e) {
            throw new SsyxException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
//        2 请求微信服务， 返回session_key 和 openid
        JSONObject jsonObject = JSONObject.parseObject(result);
        String session_key = jsonObject.getString("session_key");
        String openid = jsonObject.getString("openid");

//        3 如果是第一次登录，添加微信信息到数据库。
        User user = userService.getUserByOpenId(openid);
        if (user == null) {
            user = new User();
            user.setOpenId(openid);
            user.setNickName(openid);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER.getCode());
            user.setIsNew(0);
            userService.save(user);
        }

//        4 根据userid 查询提货点和团长信息
        LeaderAddressVo vo = userService.getLeaderAddressByUserId(user.getId());
        String token = JwtHelper.createToken(user.getId(), user.getNickName());

//       获取当前登录用户的信息 存放到redis中去，设置有效时间
        UserLoginVo userLoginVo = userService.getUserLoginVo(user);
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX + user.getId(),
                userLoginVo,
                RedisConst.USERKEY_TIMEOUT,
                TimeUnit.DAYS);

//        封装数据到map返回
        Map<String, Object> map = new HashMap<>(10);
        map.put("user", user);
        map.put("token", token);
        map.put("LeaderAddressVo", vo);
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation(value = "更新用户昵称与头像")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok(null);
    }


}
