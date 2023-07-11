package com.zml.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.product.mapper.SkuAttrValueMapper;
import com.zml.product.service.SkuAttrValueService;
import com.zml.ssyx.model.product.SkuAttrValue;

import com.zml.ssyx.model.product.SkuImage;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.product.SkuInfoVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZHANGMINLEI
 * @description 针对表【sku_attr_value(spu属性值)】的数据库操作Service实现
 * @createDate 2023-07-11 14:01:33
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue>
        implements SkuAttrValueService {


    @Override
    public List<SkuAttrValue> getAttrValuesList(Long id) {
        LambdaQueryWrapper<SkuAttrValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuAttrValue::getSkuId, id);
        return baseMapper.selectList(wrapper);
    }
}




