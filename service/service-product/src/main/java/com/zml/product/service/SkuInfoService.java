package com.zml.product.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.product.SkuInfoQueryVo;
import com.zml.ssyx.vo.product.SkuInfoVo;

import java.util.List;

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

    void chek(Long id, Integer status);

    void publish(Long id, Integer status);

    Boolean isNewPerson(Long id, Integer status);

    void deleteById(Long id);

    List<SkuInfo> findSkuInfoByKeyWord(String keyWord);

    List<SkuInfo> findNewPersonSkuInfoList();


    SkuInfoVo getSkuInfoVo(Long skuId);
}
