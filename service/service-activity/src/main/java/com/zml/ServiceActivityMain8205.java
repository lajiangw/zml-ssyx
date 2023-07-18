package com.zml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-14 10:24
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ServiceActivityMain8205 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceActivityMain8205.class, args);
    }
}
