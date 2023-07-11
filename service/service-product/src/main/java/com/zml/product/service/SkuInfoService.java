package com.zml.product.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.product.SkuInfoQueryVo;
import com.zml.ssyx.vo.product.SkuInfoVo;

/**
* @author ZHANGMINLEI
* @description 针对表【sku_info(sku信息)】的数据库操作Service
* @createDate 2023-07-11 14:01:34
*/
public interface SkuInfoService extends IService<SkuInfo> {

    IPage<SkuInfo> getList(Page<SkuInfo> skuInfoPage, SkuInfoQueryVo vo);

    void saveSkuInfo(SkuInfoVo skuInfoVo);

    SkuInfoVo getSkuInfo(Long id);

    Boolean updateSku(SkuInfoVo skuInfoVo);
}
