package com.zml.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.result.Result;
import com.zml.ssyx.model.sys.RegionWare;
import com.zml.ssyx.vo.sys.RegionWareQueryVo;

/**
* @author ZHANGMINLEI
* @description 针对表【region_ware(城市仓库关联表)】的数据库操作Service
* @createDate 2023-07-10 21:14:27
*/
public interface RegionWareService extends IService<RegionWare> {

    IPage<RegionWare> selectPage(Page<RegionWare> regionWarePage, RegionWareQueryVo regionWareQueryVo);

    void insert(RegionWare regionWare);

    void updateStatus(Long id, Integer status);
}
