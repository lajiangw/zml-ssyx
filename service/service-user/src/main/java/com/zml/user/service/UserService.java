package com.zml.user.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zml.ssyx.model.user.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.vo.user.LeaderAddressVo;
import com.zml.ssyx.vo.user.UserLoginVo;

/**
* @author ZHANGMINLEI
* @description 针对表【user(会员表)】的数据库操作Service
* @createDate 2023-07-17 12:42:53
*/
public interface UserService extends IService<User> {

    User getUserByOpenId(String openid);

    LeaderAddressVo getLeaderAddressByUserId(Long userId);

    UserLoginVo getUserLoginVo(User user);

}
