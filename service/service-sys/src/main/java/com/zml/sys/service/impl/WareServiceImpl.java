package com.zml.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.ssyx.vo.product.WareQueryVo;
import com.zml.sys.mapper.WareMapper;
import com.zml.sys.service.WareService;
import com.zml.ssyx.model.sys.Ware;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author ZHANGMINLEI
 * @description 针对表【ware(仓库表)】的数据库操作Service实现
 * @createDate 2023-07-10 21:14:31
 */
@Service
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware>
        implements WareService {

    @Override
    public IPage<Ware> getPageList(Page<Ware> warePage, WareQueryVo vo) {
        LambdaQueryWrapper<Ware> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(vo.getName()), Ware::getName, vo.getName());
        return baseMapper.selectPage(warePage, wrapper);
    }
}




