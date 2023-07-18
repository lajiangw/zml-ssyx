package com.zml.home.service.impl;

import com.zml.client.product.ProductFeignClient;
import com.zml.client.search.SearchFeignClient;
import com.zml.client.user.UserFeignClient;
import com.zml.home.service.HomeService;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.model.search.SkuEs;
import com.zml.ssyx.vo.user.LeaderAddressVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 17:07
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private SearchFeignClient searchFeignClient;

    @Override
    public Map<String, Object> homeDate(Long userId) {
//        根据userId查出当前登录用户提供的地址信息
//        通过远程调用
        Map<String, Object> map = new HashMap<>();
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);
        map.put("leaderAddressVo", leaderAddressVo);

//        通过远程调用获得所有接口分类
         List<Category> categoryList = productFeignClient.findAllCategoryList();
        map.put("categoryList", categoryList);

//        通过远程调用获取新人专项商品
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        map.put("newPersonSkuInfoList", newPersonSkuInfoList);

//        远程获取爆款商品
        List<SkuEs> hostSkuList = searchFeignClient.findHostSkuList();
        map.put("hostSkuList", hostSkuList);
        return map;
    }
}
