package com.zml.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.product.mapper.SkuStockHistoryMapper;
import com.zml.product.service.SkuStockHistoryService;
import com.zml.ssyx.model.product.SkuStockHistory;
import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【sku_stock_history(sku的库存历史记录)】的数据库操作Service实现
* @createDate 2023-07-11 14:01:34
*/
@Service
public class SkuStockHistoryServiceImpl extends ServiceImpl<SkuStockHistoryMapper, SkuStockHistory>
    implements SkuStockHistoryService{

}




