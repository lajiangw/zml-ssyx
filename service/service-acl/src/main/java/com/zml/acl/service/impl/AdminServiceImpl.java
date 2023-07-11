package com.zml.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.acl.mapper.AdminMapper;
import com.zml.acl.service.AdminService;
import com.zml.ssyx.model.acl.Admin;
import com.zml.ssyx.vo.acl.AdminQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author ZHANGMINLEI
 * @description 针对表【admin(用户表)】的数据库操作Service实现
 * @createDate 2023-07-09 21:16:30
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
        implements AdminService {


    @Override
    public IPage<Admin> getPageList(Page<Admin> adminPage, AdminQueryVo adminQueryVo) {
        String name = adminQueryVo.getName();
        String username = adminQueryVo.getUsername();
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(username), Admin::getUsername, username);
        wrapper.like(!StringUtils.isEmpty(name), Admin::getName, name);
        return baseMapper.selectPage(adminPage, wrapper);
    }
}




