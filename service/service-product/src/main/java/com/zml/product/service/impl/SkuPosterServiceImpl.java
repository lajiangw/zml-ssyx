package com.zml.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.product.mapper.SkuPosterMapper;
import com.zml.product.service.SkuPosterService;
import com.zml.ssyx.model.product.SkuImage;
import com.zml.ssyx.model.product.SkuPoster;

import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【sku_poster(商品海报表)】的数据库操作Service实现
* @createDate 2023-07-11 14:01:34
*/
@Service
public class SkuPosterServiceImpl extends ServiceImpl<SkuPosterMapper, SkuPoster>
    implements SkuPosterService{

    @Override
    public List<SkuPoster> getPosterList(Long id) {
        LambdaQueryWrapper<SkuPoster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuPoster::getSkuId, id);
        return baseMapper.selectList(wrapper);
    }
}




