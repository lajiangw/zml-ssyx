package com.zml.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.product.mapper.AttrMapper;
import com.zml.product.service.AttrService;

import com.zml.ssyx.model.product.Attr;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZHANGMINLEI
 * @description 针对表【attr(商品属性)】的数据库操作Service实现
 * @createDate 2023-07-11 14:01:33
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr>
        implements AttrService {

    @Override
    public List<Attr> getList(Long groupId) {
        LambdaQueryWrapper<Attr> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attr::getAttrGroupId, groupId);
        return baseMapper.selectList(wrapper);
    }
}




