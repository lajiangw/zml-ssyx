package com.zml.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zml.product.service.SkuAttrValueService;
import com.zml.product.service.SkuInfoService;
import com.zml.result.Result;
import com.zml.ssyx.model.product.SkuInfo;
import com.zml.ssyx.vo.product.SkuInfoQueryVo;
import com.zml.ssyx.vo.product.SkuInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-11 16:49
 */

@RestController
@Api(value = "SkuInfo管理", tags = "商品Sku管理")
@RequestMapping("/admin/product/skuInfo")
//@CrossOrigin
public class SkuInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    @Resource
    private SkuAttrValueService skuAttrValueService;

    @GetMapping("/{page}/{limit}")
    @ApiOperation("分页查询")
    public Result getList(@PathVariable Long page, @PathVariable Long limit, SkuInfoQueryVo vo) {
        Page<SkuInfo> skuInfoPage = new Page<>(page, limit);
        IPage<SkuInfo> iPage = skuInfoService.getList(skuInfoPage, vo);
        return Result.ok(iPage);
    }

    @ApiOperation("保存")
    @PostMapping("/save")
//    TODO 阿里云有学生认证七个月免费服务器，项目需要课设记得学生认证并上线阿里云
    public Result save(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok();
    }

    @ApiOperation("获取sku信息")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SkuInfoVo vo = skuInfoService.getSkuInfo(id);
        return Result.ok(vo);
    }

    @ApiOperation("修改sku")
    @PutMapping("/update")
    public Result update(@RequestBody SkuInfoVo skuInfoVo) {
        return skuInfoService.updateSku(skuInfoVo) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id) {
        skuInfoService.deleteById(id);
        return Result.ok();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(List<Long> idList) {
        return skuInfoService.removeByIds(idList) ? Result.ok() : Result.fail();
    }

    @ApiOperation("商品审核")
    @GetMapping("/check/{id}/{status}")
    public Result check(@PathVariable Long id, @PathVariable Integer status) {
        skuInfoService.chek(id, status);
        return Result.ok();
    }

    @ApiOperation("商品上架")
    @GetMapping("/publish/{id}/{status}")
    public Result publish(@PathVariable Long id, @PathVariable Integer status) {
        skuInfoService.publish(id, status);
        return Result.ok();
    }

    @ApiOperation("新人专项")
    @GetMapping("/isNewPerson/{id}/{status}")
    public Result isNewPerson(@PathVariable Long id, @PathVariable Integer status) {
        return skuInfoService.isNewPerson(id, status) ? Result.ok() : Result.fail();
    }


}

