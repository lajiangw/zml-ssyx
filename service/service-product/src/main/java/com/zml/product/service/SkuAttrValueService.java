package com.zml.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.product.SkuAttrValue;
import com.zml.ssyx.vo.product.SkuInfoVo;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【sku_attr_value(spu属性值)】的数据库操作Service
* @createDate 2023-07-11 14:01:33
*/
public interface SkuAttrValueService extends IService<SkuAttrValue> {


    List<SkuAttrValue> getAttrValuesList(Long id);
}
