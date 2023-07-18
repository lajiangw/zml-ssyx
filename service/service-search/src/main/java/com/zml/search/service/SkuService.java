package com.zml.search.service;

import com.zml.ssyx.model.search.SkuEs;
import com.zml.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo);
}
