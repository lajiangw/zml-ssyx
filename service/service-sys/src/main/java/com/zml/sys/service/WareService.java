package com.zml.sys.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.sys.Ware;
import com.zml.ssyx.vo.product.WareQueryVo;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【ware(仓库表)】的数据库操作Service
* @createDate 2023-07-10 21:14:31
*/
public interface WareService extends IService<Ware> {

    IPage<Ware> getPageList(Page<com.zml.ssyx.model.sys.Ware> warePage, WareQueryVo vo);
}
