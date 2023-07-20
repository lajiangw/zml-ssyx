package com.zml.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.constant.RedisConst;
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
import com.zml.ssyx.vo.product.SkuStockLockVo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

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

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

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
        wrapper.eq(SkuInfo::getIsNewPerson, 1).eq(SkuInfo::getPublishStatus, 1).orderByAsc(SkuInfo::getStock);
        return baseMapper.selectPage(skuInfoPage, wrapper).getRecords();

    }

    @Override
    public SkuInfoVo getSkuInfoVo(Long skuId) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();
//            查询基本信息
        SkuInfo skuInfo = baseMapper.selectById(skuId);

//        查询图片
        List<SkuImage> imageList = skuImageService.getImageList(skuId);

//        查询海报
        List<SkuPoster> posterList = skuPosterService.getPosterList(skuId);

//        查询属性
        List<SkuAttrValue> attrValuesList = skuAttrValueService.getAttrValuesList(skuId);
        BeanUtils.copyProperties(skuInfo, skuInfoVo);
        skuInfoVo.setSkuPosterList(posterList);
        skuInfoVo.setSkuImagesList(imageList);
        skuInfoVo.setSkuAttrValueList(attrValuesList);
        return skuInfoVo;
    }

    @Override
    public Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo) {
        //        判断集合是否为空，为空则直接返回
        if (CollectionUtils.isEmpty(skuStockLockVoList)) {
            return null;
        }
//        遍历集合，验证库存，锁定库存 具有原子性
        skuStockLockVoList.forEach(skuStockLockVo -> {
            this.checkLock(skuStockLockVo);
        });
//        只要有一个商品锁定失败，所有锁定的商品都解锁
        boolean match = skuStockLockVoList.stream().anyMatch(skuStockLockVo -> !skuStockLockVo.getIsLock());
        if (match) {
//            解锁
            skuStockLockVoList.stream().filter(SkuStockLockVo::getIsLock).forEach(skuStockLockVo -> baseMapper.unlockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum()));
            return false;
        }
//        4如果所有的商品都锁定成功，redis 缓存相关数据，为了方便后边解锁和减库存
        redisTemplate.opsForValue().set(RedisConst.SROCK_INFO + orderNo, skuStockLockVoList);
        //    验证和锁定库存
        return true;
    }

    private void checkLock(SkuStockLockVo skuStockLockVo) {
//        公平锁，在线程中等待时间最长的得到锁
        RLock rLock = this.redissonClient.getFairLock(RedisConst.SKUKEY_PREFIX + skuStockLockVo.getSkuId());

//        加锁
        rLock.lock();
        try {
//      验证库存
            SkuInfo skuInfo = baseMapper.chekStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
//           没有满足要求的商品直接返回
            if (skuInfo == null) {
                skuStockLockVo.setIsLock(false);
                return;
            }
//           有满足的商品
//          更新操作
            if (baseMapper.lockStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum()) > 0) {
                skuStockLockVo.setIsLock(true);
            }
        } finally {
//            就算失败也会 解锁
            rLock.unlock();
        }

    }


}




