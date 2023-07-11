package com.zml.acl.mapper;

import com.zml.ssyx.model.acl.Admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ZHANGMINLEI
* @description 针对表【admin(用户表)】的数据库操作Mapper
* @createDate 2023-07-09 21:16:30
* @Entity generator.domain.Admin
*/
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}




