package com.zml.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.acl.Role;
import com.zml.ssyx.vo.acl.RoleQueryVo;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 18:13
 */
public interface RoleService extends IService<Role> {
    IPage selectRolePage(Page<Role> rolePage, RoleQueryVo roleQueryVo);
}
