package com.zml.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.acl.mapper.RolePermissionMapper;
import com.zml.acl.service.RolePermissionService;
import com.zml.ssyx.model.acl.RolePermission;

import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【role_permission(角色权限)】的数据库操作Service实现
* @createDate 2023-07-10 19:25:11
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService {

}




