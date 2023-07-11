package com.zml.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.product.mapper.CategoryMapper;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.vo.product.CategoryQueryVo;

import com.zml.product.service.CategoryService;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author ZHANGMINLEI
 * @description 针对表【category(商品三级分类)】的数据库操作Service实现
 * @createDate 2023-07-11 14:01:33
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {


    @Override
    public IPage<Category> pageList(Page<Category> categoryPage, CategoryQueryVo vo) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(vo.getName()), Category::getName, vo.getName());
        return  baseMapper.selectPage(categoryPage, wrapper);
    }
}




