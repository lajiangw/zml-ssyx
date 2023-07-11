package com.zml.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.exception.SsyxException;
import com.zml.result.Result;
import com.zml.result.ResultCodeEnum;
import com.zml.ssyx.vo.sys.RegionWareQueryVo;
import com.zml.sys.mapper.RegionWareMapper;
import com.zml.sys.mapper.WareMapper;
import com.zml.sys.service.RegionWareService;
import com.zml.ssyx.model.sys.RegionWare;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author ZHANGMINLEI
 * @description 针对表【region_ware(城市仓库关联表)】的数据库操作Service实现
 * @createDate 2023-07-10 21:14:27
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare>
        implements RegionWareService {

    @Override
    public IPage<RegionWare> selectPage(Page<RegionWare> regionWarePage, RegionWareQueryVo regionWareQueryVo) {
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        String keyword = regionWareQueryVo.getKeyword();
        wrapper.like(!StringUtils.isEmpty(keyword), RegionWare::getRegionName, keyword).or().like(!StringUtils.isEmpty(keyword), RegionWare::getWareName, keyword);
        return baseMapper.selectPage(regionWarePage, wrapper);
    }

    @Override
    public void insert(RegionWare regionWare) {
//        先查看是否已经开通
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionWare::getRegionId, regionWare.getRegionId());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new SsyxException(ResultCodeEnum.REGION_OPEN);
        }
        baseMapper.insert(regionWare);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);
    }
}




