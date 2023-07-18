package com.zml.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.ssyx.model.user.Leader;
import com.zml.user.mapper.LeaderMapper;
import com.zml.user.service.LeaderService;

import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【leader(团长表)】的数据库操作Service实现
* @createDate 2023-07-17 13:23:29
*/
@Service
public class LeaderServiceImpl extends ServiceImpl<LeaderMapper, Leader>
    implements LeaderService {

}




