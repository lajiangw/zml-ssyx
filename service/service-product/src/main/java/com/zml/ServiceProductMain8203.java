package com.zml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-11 14:00
 */
@SpringBootApplication
@EnableDiscoveryClient
@FeignClient
public class ServiceProductMain8203 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProductMain8203.class, args);
    }
}
