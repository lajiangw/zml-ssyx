package com.zml.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.ssyx.model.user.Leader;
import com.zml.ssyx.model.user.User;
import com.zml.ssyx.model.user.UserDelivery;
import com.zml.ssyx.vo.user.LeaderAddressVo;
import com.zml.ssyx.vo.user.UserLoginVo;
import com.zml.user.mapper.LeaderMapper;
import com.zml.user.mapper.UserDeliveryMapper;
import com.zml.user.mapper.UserMapper;
import com.zml.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ZHANGMINLEI
 * @description 针对表【user(会员表)】的数据库操作Service实现
 * @createDate 2023-07-17 12:42:53
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private LeaderMapper leaderMapper;
    @Resource
    private UserDeliveryMapper userDeliveryMapper;

    @Override
    public User getUserByOpenId(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenId, openid);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public LeaderAddressVo getLeaderAddressByUserId(Long userId) {
        UserDelivery userDelivery = userDeliveryMapper.selectOne(
                new LambdaQueryWrapper<UserDelivery>().eq(UserDelivery::getUserId, userId)
                        .eq(UserDelivery::getIsDeleted, 0));
        if (userDelivery == null) {
            return null;
        }
        Long leaderId = userDelivery.getLeaderId();
        Leader leader = leaderMapper.selectById(leaderId);

        LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
        BeanUtil.copyProperties(leader, leaderAddressVo);
        leaderAddressVo.setUserId(userId);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    @Override
    public UserLoginVo getUserLoginVo(User user) {
        UserLoginVo userLoginVo = new UserLoginVo();
        BeanUtil.copyProperties(user, userLoginVo);

        UserDelivery userDelivery = userDeliveryMapper.selectOne(new LambdaQueryWrapper<UserDelivery>()
                .eq(UserDelivery::getUserId, user.getId()).eq(UserDelivery::getIsDeleted, 1));
        if (userDelivery != null) {
            userLoginVo.setWareId(userDelivery.getWareId());
            userLoginVo.setLeaderId(userDelivery.getLeaderId());
        } else {
            userLoginVo.setLeaderId(1L);
            userLoginVo.setWareId(1L);
        }
        return userLoginVo;
    }
}




