package com.zml.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.product.Attr;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【attr(商品属性)】的数据库操作Service
* @createDate 2023-07-11 14:01:33
*/
public interface AttrService extends IService<Attr> {

    List<Attr> getList(Long groupId);
}
