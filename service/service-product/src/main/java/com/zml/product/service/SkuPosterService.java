package com.zml.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.product.SkuPoster;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【sku_poster(商品海报表)】的数据库操作Service
* @createDate 2023-07-11 14:01:34
*/
public interface SkuPosterService extends IService<SkuPoster> {

    List<SkuPoster> getPosterList(Long id);
}
