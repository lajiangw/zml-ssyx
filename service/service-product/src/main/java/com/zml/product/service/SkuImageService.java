package com.zml.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.product.SkuImage;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【sku_image(商品图片)】的数据库操作Service
* @createDate 2023-07-11 14:01:34
*/
public interface SkuImageService extends IService<SkuImage> {

    List<SkuImage> getImageList(Long id);
}
