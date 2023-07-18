package com.zml.search.service.impl;

import com.zml.client.product.ProductFeignClient;
import com.zml.search.repository.SkuRepository;
import com.zml.search.service.SkuService;
import com.zml.ssyx.enums.SkuType;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 16:25
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Resource
    private SkuRepository skuRepository;

    @Resource
    private ProductFeignClient productFeignClient;

    @Override
    public void upperSku(Long skuId) {
        SkuEs skuEs = new SkuEs();
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (Objects.isNull(skuInfo)) return;

        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        if (!Objects.isNull(category)) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName() + "," + skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if (Objects.equals(skuInfo.getSkuType(), SkuType.COMMON.getCode())) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        }
        skuRepository.save(skuEs);
    }

    @Override
    public void lowerSku(Long skuId) {
        skuRepository.deleteById(skuId);
    }

    @Override
    public void deleteById(Long id) {
        skuRepository.deleteById(id);
    }

    @Override
    public List<SkuEs> findHostSkuList() {
        Pageable pageable = PageRequest.of(0, 10);
        return skuRepository.findByOrderByHotScoreDesc(pageable).getContent();
    }

}
