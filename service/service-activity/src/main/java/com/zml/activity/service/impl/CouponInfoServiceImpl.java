package com.zml.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zml.activity.mapper.CouponInfoMapper;
import com.zml.activity.mapper.CouponRangeMapper;
import com.zml.activity.service.CouponInfoService;
import com.zml.client.product.ProductFeignClient;
import com.zml.ssyx.enums.CouponRangeType;
import com.zml.ssyx.model.activity.CouponInfo;
import com.zml.ssyx.model.activity.CouponRange;
import com.zml.ssyx.model.product.Category;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.activity.CouponRuleVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZHANGMINLEI
 * @description 针对表【coupon_info(优惠券信息)】的数据库操作Service实现
 * @createDate 2023-07-14 10:28:46
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo>
        implements CouponInfoService {

    @Resource
    private CouponRangeMapper couponRangeMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    @Override
    public IPage<CouponInfo> getPageList(Page<CouponInfo> couponInfoPage) {
        Page<CouponInfo> couponInfoPage1 = baseMapper.selectPage(couponInfoPage, null);
        List<CouponInfo> records = couponInfoPage1.getRecords();
        records.forEach(item -> {
            item.setCouponName(item.getCouponType().getComment());
            CouponRangeType rangeType = item.getRangeType();
            if (rangeType != null) {
                item.setRangeTypeString(rangeType.getComment());
            }
        });
        return couponInfoPage1;
    }

    @Override
    public CouponInfo selectById(Long id) {
        CouponInfo couponInfo = baseMapper.selectById(id);
        couponInfo.setCouponTypeString(couponInfo.getCouponType().getComment());
        if (couponInfo.getRangeType() != null) {
            couponInfo.setRangeTypeString(couponInfo.getRangeType().getComment());
        }
        return couponInfo;
    }

    @Override
    public Map<String, Object> findCouponRuleList(Long id) {
        Map<String, Object> map = new HashMap<>();
        CouponInfo couponInfo = baseMapper.selectById(id);

        List<CouponRange> couponRanges = couponRangeMapper.
                selectList(new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, id));

        List<Long> rangeIds = couponRanges.stream().map(CouponRange::getRangeId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rangeIds)) {
            if (couponInfo.getRangeType() == CouponRangeType.SKU) {
                List<SkuInfo> skuInfos = productFeignClient.getSkuInfos(rangeIds);
                map.put("skuInfoList", skuInfos);
            } else if (couponInfo.getRangeType() == CouponRangeType.CATEGORY) {
                List<Category> categories = productFeignClient.findCateGoryList(rangeIds);
                map.put("categoryList", categories);
            }
        }
        return map;
    }

    @Override
    @Transactional
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
//        删除之前的信息
        couponRangeMapper.delete(new LambdaQueryWrapper<CouponRange>().
                eq(CouponRange::getCouponId, couponRuleVo.getCouponId()));
//更新优惠卷信息
        CouponInfo couponInfo = baseMapper.selectById(couponRuleVo.getCouponId());
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());

        baseMapper.updateById(couponInfo);
//添加优惠卷新规则
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        couponRangeList.forEach(item -> {
            item.setCouponId(couponRuleVo.getCouponId());
            couponRangeMapper.insert(item);
        });
    }

    //    根据skuid  和userid查询优惠卷信息
    @Override
    public List<CouponInfo> findCouponInfoList(Long skuId, Long userId) {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
       return baseMapper.selectCouponList(skuInfo.getId(), skuInfo.getCategoryId(), userId);
    }
}




