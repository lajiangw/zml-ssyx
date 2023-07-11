package com.zml.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.ssyx.model.acl.Admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.vo.acl.AdminQueryVo;

/**
* @author ZHANGMINLEI
* @description 针对表【admin(用户表)】的数据库操作Service
* @createDate 2023-07-09 21:16:30
*/
public interface AdminService extends IService<Admin> {



    IPage<Admin> getPageList(Page<Admin> adminPage, AdminQueryVo vo);
}
