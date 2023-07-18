package com.zml.product.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.product.mapper.RegionWareMapper;
import com.zml.ssyx.model.sys.RegionWare;

import org.springframework.stereotype.Service;

/**
* @author ZHANGMINLEI
* @description 针对表【region_ware(城市仓库关联表)】的数据库操作Service实现
* @createDate 2023-07-11 14:01:33
*/
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare>
    implements IService<RegionWare> {

}




