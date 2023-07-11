package com.zml.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.product.mapper.AttrGroupMapper;

import com.zml.product.service.AttrGroupService;

import com.zml.result.Result;
import com.zml.ssyx.model.product.AttrGroup;
import com.zml.ssyx.vo.product.AttrGroupQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author ZHANGMINLEI
 * @description 针对表【attr_group(属性分组)】的数据库操作Service实现
 * @createDate 2023-07-11 14:01:33
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup>
        implements AttrGroupService {

    @Override
    public IPage<AttrGroup> pageList(Page<AttrGroup> attrGroupPage, AttrGroupQueryVo vo) {
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(vo.getName()), AttrGroup::getName, vo.getName());
        return baseMapper.selectPage(attrGroupPage, wrapper);
    }

    @Override
    public List<AttrGroup> findAllList() {
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AttrGroup::getId);
       return baseMapper.selectList(wrapper);
    }


}




