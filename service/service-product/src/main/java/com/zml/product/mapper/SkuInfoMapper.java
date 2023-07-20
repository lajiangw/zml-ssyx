package com.zml.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zml.ssyx.model.product.SkuInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author ZHANGMINLEI
 * @description 针对表【sku_info(sku信息)】的数据库操作Mapper
 * @createDate 2023-07-11 14:01:34
 * @Entity generator.domain.SkuInfo
 */
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    void unlockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    SkuInfo chekStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    Integer lockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);
}




