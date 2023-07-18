package com.zml.search.service;

import com.zml.ssyx.model.search.SkuEs;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 16:25
 */
public interface SkuService {
    void upperSku(Long skuId);

    void lowerSku(Long skuId);

    void deleteById(Long id);

    List<SkuEs> findHostSkuList();
}
