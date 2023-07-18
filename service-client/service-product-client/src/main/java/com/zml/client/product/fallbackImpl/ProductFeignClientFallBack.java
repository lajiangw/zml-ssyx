package com.zml.client.product.fallbackImpl;

import com.zml.client.product.ProductFeignClient;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.product.SkuInfoVo;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 17:31
 */

@Component
public class ProductFeignClientFallBack implements ProductFeignClient {
    @Override
    public Category getCategory(Long categoryId) {
        return null;
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        return null;
    }

    @Override
    public List<SkuInfo> getSkuInfos(List<Long> ids) {
        return null;
    }

    @Override
    public List<SkuInfo> findSkuInfoByKeyWord(String keyWord) {
        return null;
    }

    @Override
    public List<Category> findCateGoryList(List<Long> ids) {
        return null;
    }

    @Override
    public List<Category> findAllCategoryList() {
        return null;
    }

    @Override
    public List<SkuInfo> findNewPersonSkuInfoList() {
        return null;
    }

    @Override
    public SkuInfoVo getSkuInfoVo(Long skuId) {
        return null;
    }
}
