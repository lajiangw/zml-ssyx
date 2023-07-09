package com.zml.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.acl.mapper.RoleMapper;
import com.zml.acl.service.RoleService;
import com.zml.ssyx.model.acl.Role;
import com.zml.ssyx.vo.acl.RoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 18:14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {



    @Override
    public IPage<Role> selectRolePage(Page<Role> rolePage, RoleQueryVo roleQueryVo) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        String roleName = roleQueryVo.getRoleName();
//        构造条件，只有name不为空，才进行条件匹配
        wrapper.like(!StringUtils.isEmpty(roleName), Role::getRoleName, roleName);
//        将分页条件传入，和条件 进行查询返回。
        return baseMapper.selectPage(rolePage, wrapper);

    }
}
