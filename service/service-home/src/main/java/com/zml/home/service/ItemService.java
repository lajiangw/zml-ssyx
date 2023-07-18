package com.zml.home.service;

import java.util.Map;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 17:12
 */
public interface ItemService {
    Map<String, Object> item(Long id, Long userId);
}
