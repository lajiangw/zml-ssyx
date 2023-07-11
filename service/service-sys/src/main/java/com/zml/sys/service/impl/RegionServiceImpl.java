package com.zml.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.sys.mapper.RegionMapper;

import com.zml.sys.service.RegionService;

import com.zml.ssyx.model.sys.Region;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ZHANGMINLEI
* @description 针对表【region(地区表)】的数据库操作Service实现
* @createDate 2023-07-10 21:14:13
*/
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region>
    implements RegionService {

    @Override
    public List<Region> findRegionByKeyword(String keyword) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Region::getName,keyword);
        return baseMapper.selectList(wrapper);
    }
}




