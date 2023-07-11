package com.zml.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.acl.mapper.AdminRoleMapper;
import com.zml.acl.service.AdminRoleService;

import com.zml.ssyx.model.acl.AdminRole;
import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【admin_role(用户角色)】的数据库操作Service实现
* @createDate 2023-07-10 14:15:12
*/
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole>
    implements AdminRoleService {

}




