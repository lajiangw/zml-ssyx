package com.zml.client.search.fallback;

import com.zml.client.search.SearchFeignClient;
import com.zml.ssyx.model.search.SkuEs;

import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 19:06
 */
public class SearchFeignClientImpl implements SearchFeignClient {
    @Override
    public List<SkuEs> findHostSkuList() {
        System.out.println("findHostSkuList错误！");
        return null;
    }

    @Override
    public boolean incrHotScore(Long skuId) {
        return false;
    }
}
