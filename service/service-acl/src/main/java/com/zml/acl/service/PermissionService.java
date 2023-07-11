package com.zml.acl.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.acl.Permission;

import java.util.List;
import java.util.Map;

/**
 * @author ZHANGMINLEI
 * @description 针对表【permission(权限)】的数据库操作Service
 * @createDate 2023-07-10 16:45:50
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> queryAllPermission();

    void removeChildById(Long id);

    List<Permission> toAssign(Long roleId);

    void doAssign(Long roleId, List<Long> permissionId);

}
