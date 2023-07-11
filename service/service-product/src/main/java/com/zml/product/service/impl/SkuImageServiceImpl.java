package com.zml.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.product.mapper.SkuImageMapper;
import com.zml.product.service.SkuImageService;
import com.zml.ssyx.model.product.SkuImage;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZHANGMINLEI
 * @description 针对表【sku_image(商品图片)】的数据库操作Service实现
 * @createDate 2023-07-11 14:01:34
 */
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage>
        implements SkuImageService {


    //        根据id查询商品图片列表
    @Override
    public List<SkuImage> getImageList(Long id) {
        LambdaQueryWrapper<SkuImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getSkuId, id);
        return baseMapper.selectList(wrapper);
    }
}




