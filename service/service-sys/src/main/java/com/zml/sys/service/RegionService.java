package com.zml.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.sys.Region;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【region(地区表)】的数据库操作Service
* @createDate 2023-07-10 21:14:13
*/
public interface RegionService extends IService<Region> {

    List<Region> findRegionByKeyword(String keyword);
}
