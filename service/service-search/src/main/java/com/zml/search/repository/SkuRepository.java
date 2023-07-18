package com.zml.search.repository;

import com.zml.ssyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 16:27
 */
public interface SkuRepository extends ElasticsearchRepository<SkuEs, Long> {

   Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);

    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, Pageable pageable);


    Page<SkuEs> findByWareIdAndKeyword(Long wareId, String keyword, Pageable pageable);
}
