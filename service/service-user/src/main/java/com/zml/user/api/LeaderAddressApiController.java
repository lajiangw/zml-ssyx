package com.zml.user.api;

import com.zml.ssyx.vo.user.LeaderAddressVo;
import com.zml.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 17:47
 */
@RestController
@RequestMapping("/api/user/leader")
public class LeaderAddressApiController {

    @Resource
    private UserService userService;

    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable Long userId) {
        return userService.getLeaderAddressByUserId(userId);
    }

}


