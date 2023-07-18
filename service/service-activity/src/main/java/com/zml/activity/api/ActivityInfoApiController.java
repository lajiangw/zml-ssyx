package com.zml.activity.api;

import com.zml.activity.service.ActivityInfoService;
import com.zml.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 11:56
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityInfoApiController {

    @Resource
    private ActivityInfoService activityInfoService;

    @PostMapping("/inner/findActivity")
    public Map<Long,List<String>> findActivity(@RequestBody List<Long> skuIdList){
      return   activityInfoService.findActivity(skuIdList);
    }
}
