package com.zml.auth;

import com.zml.constant.RedisConst;
import com.zml.ssyx.vo.user.UserLoginVo;
import com.zml.utils.JwtHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 15:57
 */

public class UserLoginInterceptor implements HandlerInterceptor {
    private RedisTemplate redisTemplate;

    public UserLoginInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.initUserLoginVo(request);
        return true;
    }

    private void initUserLoginVo(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            Long userId = JwtHelper.getUserId(token);
            UserLoginVo loginVo = (UserLoginVo) redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY_PREFIX + userId);
            if (!StringUtils.isEmpty(loginVo)) {
                AuthContextHolder.setUserId(userId);
                AuthContextHolder.setUserLoginVo(loginVo);
                AuthContextHolder.setWareId(loginVo.getWareId());
            }
        }
    }
}
