package com.zml.product.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.result.Result;
import com.zml.ssyx.model.product.AttrGroup;
import com.zml.ssyx.vo.product.AttrGroupQueryVo;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【attr_group(属性分组)】的数据库操作Service
* @createDate 2023-07-11 14:01:33
*/
public interface AttrGroupService extends IService<AttrGroup> {

    IPage<AttrGroup> pageList(Page<AttrGroup> attrGroupPage, AttrGroupQueryVo vo);

    List<AttrGroup> findAllList();


}
