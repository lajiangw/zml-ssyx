package com.zml.product.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.vo.product.CategoryQueryVo;

/**
* @author ZHANGMINLEI
* @description 针对表【category(商品三级分类)】的数据库操作Service
* @createDate 2023-07-11 14:01:33
*/
public interface CategoryService extends IService<Category> {

    IPage<Category> pageList(Page<Category> categoryPage, CategoryQueryVo vo);
}
