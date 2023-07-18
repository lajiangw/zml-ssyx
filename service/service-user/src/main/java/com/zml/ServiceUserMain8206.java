package com.zml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-17 10:53
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceUserMain8206 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserMain8206.class, args);
    }
}

