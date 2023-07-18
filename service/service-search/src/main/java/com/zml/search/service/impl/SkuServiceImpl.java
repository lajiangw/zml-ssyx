package com.zml.search.service.impl;

import com.zml.activity.client.ActivityFeignClient;
import com.zml.auth.AuthContextHolder;
import com.zml.client.product.ProductFeignClient;
import com.zml.search.repository.SkuRepository;
import com.zml.search.service.SkuService;
import com.zml.ssyx.enums.SkuType;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.model.search.SkuEs;
import com.zml.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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

    @Resource
    private ActivityFeignClient activityFeignClient;

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

    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
//        设置仓库id
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        //      判断keyword是为空，
        //为空则根据 仓库ID + 分类ID查询
        Page<SkuEs> page = null;
        String keyword = skuEsQueryVo.getKeyword();
        if (StringUtils.isEmpty(keyword)) {
            page = skuRepository.findByCategoryIdAndWareId
                    (skuEsQueryVo.getCategoryId(), skuEsQueryVo.getWareId(), pageable);
        } else {
//            不为空则根据仓库ID + 分类ID+keyword查询
            page = skuRepository.findByWareIdAndKeyword
                    (skuEsQueryVo.getWareId(), keyword, pageable);
        }

//        查询商品参加的优惠信息
        List<SkuEs> skuEsList = page.getContent();
        List<Long> collect = skuEsList.stream()
                .map(SkuEs::getId).
                collect(Collectors.toList());
//        根据skuid 进行远程调用  查询列表信息
//        封装到sku ruleList属性中去
        Map<Long, List<String>> map = null;

        map = activityFeignClient.findActivity(collect);
        if (map != null) {
            for (SkuEs skuEs : skuEsList) {
                skuEs.setRuleList(map.get(skuEs.getId()));
            }
        }
        return page;
    }

}
