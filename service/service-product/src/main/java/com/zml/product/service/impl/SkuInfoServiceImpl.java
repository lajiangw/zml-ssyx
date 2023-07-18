package com.zml.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zml.mq.constant.MqConst;
import com.zml.mq.service.RabbitService;
import com.zml.product.mapper.SkuInfoMapper;
import com.zml.product.service.SkuAttrValueService;
import com.zml.product.service.SkuImageService;
import com.zml.product.service.SkuInfoService;
import com.zml.product.service.SkuPosterService;
import com.zml.ssyx.model.product.SkuAttrValue;
import com.zml.ssyx.model.product.SkuImage;
import com.zml.ssyx.model.product.SkuInfo;

import com.zml.ssyx.model.product.SkuPoster;
import com.zml.ssyx.vo.product.SkuInfoQueryVo;
import com.zml.ssyx.vo.product.SkuInfoVo;
import javafx.scene.control.Skin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author ZHANGMINLEI
 * @description 针对表【sku_info(sku信息)】的数据库操作Service实现
 * @createDate 2023-07-11 14:01:34
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {

    //    图片
    @Autowired
    private SkuImageService skuImageService;

    //    平台属性
    @Resource
    private SkuAttrValueService skuAttrValueService;

    //    海报
    @Resource
    private SkuPosterService skuPosterService;

    @Resource
    private RabbitService rabbitService;

    @Override
    public IPage<SkuInfo> getList(Page<SkuInfo> skuInfoPage, SkuInfoQueryVo vo) {
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        Long categoryId = vo.getCategoryId();
        String keyword = vo.getKeyword();
        String skuType = vo.getSkuType();
        wrapper.like(!StringUtils.isEmpty(skuType), SkuInfo::getSkuType, skuType);
        wrapper.like(!StringUtils.isEmpty(keyword), SkuInfo::getSkuName, keyword);
        wrapper.eq(!Objects.isNull(categoryId), SkuInfo::getCategoryId, categoryId);
        return baseMapper.selectPage(skuInfoPage, wrapper);
    }

    @Override
    @Transactional
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        SkuInfo skuInfo = new SkuInfo();
        BeanUtil.copyProperties(skuInfoVo, skuInfo);
//        添加基本信息
        baseMapper.insert(skuInfo);
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
//        添加海报
        if (!CollectionUtils.isEmpty(skuPosterList)) {
            skuPosterList.forEach(skuPoster -> {
                skuPoster.setSkuId(skuInfo.getId());
            });
        }
        skuPosterService.saveBatch(skuPosterList);
//        添加图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (!CollectionUtils.isEmpty(skuImagesList)) {
            skuImagesList.forEach(skuImage -> {
                skuImage.setSkuId(skuInfo.getId());
            });
        }
        skuImageService.saveBatch(skuImagesList);

        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
//        添加平台属性
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            skuAttrValueList.forEach(item -> {
                item.setSkuId(skuInfo.getId());
            });
        }
        skuAttrValueService.saveBatch(skuAttrValueList);
    }

    @Override
    public SkuInfoVo getSkuInfo(Long id) {
        SkuInfoVo vo = new SkuInfoVo();
        SkuInfo skuInfo = baseMapper.selectById(id);
//        根据id查询商品图片列表
        List<SkuImage> skuImages = skuImageService.getImageList(id);

//        根据ID查询海报
        List<SkuPoster> skuPosterList = skuPosterService.getPosterList(id);

//        根据ID查询商品属性列表
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.getAttrValuesList(id);

//        封装所有数据进行返回。
        BeanUtil.copyProperties(skuInfo, vo);
        vo.setSkuPosterList(skuPosterList);
        vo.setSkuImagesList(skuImages);
        vo.setSkuAttrValueList(skuAttrValueList);
        return vo;
    }

    @Override
    @Transactional
    public Boolean updateSku(SkuInfoVo skuInfoVo) {
        boolean b = false;
        SkuInfo skuInfo = new SkuInfo();
        //修改基本信息
        BeanUtil.copyProperties(skuInfoVo, skuInfo);
        baseMapper.updateById(skuInfo);
        Long id = skuInfoVo.getId();
//      修改  海报信息
        LambdaQueryWrapper<SkuPoster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuPoster::getSkuId, id);
        skuPosterService.remove(wrapper);
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if (!CollectionUtils.isEmpty(skuPosterList)) {
            skuPosterList.forEach(item -> {
                item.setSkuId(skuInfo.getId());
            });
            skuPosterService.saveBatch(skuPosterList);
        }
//          修改图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        skuImageService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, id));
        if (!CollectionUtils.isEmpty(skuImagesList)) {
            skuImagesList.forEach(item -> {
                item.setSkuId(skuInfo.getId());
            });
            skuImageService.saveBatch(skuImagesList);
        }
        //  修改属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, id));
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            skuAttrValueList.forEach(item -> {
                item.setSkuId(skuInfo.getId());
            });
            b = skuAttrValueService.saveBatch(skuAttrValueList);
        }
        return b;
    }

    @Override
    public void chek(Long id, Integer status) {
        SkuInfo skuInfo = baseMapper.selectById(id);
        skuInfo.setCheckStatus(status);
        baseMapper.updateById(skuInfo);

    }

    @Override
    @Transactional
    public void publish(Long id, Integer status) {

        if (status == 1) {//商品上架
            SkuInfo skuInfo = baseMapper.selectById(id);
            skuInfo.setPublishStatus(status);
            baseMapper.updateById(skuInfo);
//            将id发送到MQ中
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_UPPER, id);
        } else {//商品下架
            SkuInfo skuInfo = baseMapper.selectById(id);
            skuInfo.setPublishStatus(status);
            baseMapper.updateById(skuInfo);
            rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_LOWER, id);
        }
    }

    @Override
    public Boolean isNewPerson(Long id, Integer status) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(id);
        skuInfo.setIsNewPerson(status);
        return baseMapper.updateById(skuInfo) > 0;
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
        rabbitService.sendMessage(MqConst.EXCHANGE_GOODS_DIRECT, MqConst.ROUTING_GOODS_DELETE, id);
    }

    @Override
    public List<SkuInfo> findSkuInfoByKeyWord(String keyWord) {
        return baseMapper.selectList(new LambdaQueryWrapper<SkuInfo>().like(SkuInfo::getSkuName, keyWord));
    }

    @Override
    public List<SkuInfo> findNewPersonSkuInfoList() {

        Page<SkuInfo> skuInfoPage = new Page<>(1, 3);

        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuInfo::getIsNewPerson, 1)
                .eq(SkuInfo::getPublishStatus, 1)
                .orderByAsc(SkuInfo::getStock);
        return baseMapper.selectPage(skuInfoPage, wrapper).getRecords();

    }
}




