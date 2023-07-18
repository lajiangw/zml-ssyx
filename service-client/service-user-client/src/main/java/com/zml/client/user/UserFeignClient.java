package com.zml.client.user;

import com.zml.ssyx.vo.user.LeaderAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 18:00
 */
@FeignClient("service-user")
public interface UserFeignClient {
    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable Long userId);
}
