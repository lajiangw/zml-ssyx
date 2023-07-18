package com.zml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-10 21:08
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceSysMain8202 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSysMain8202.class, args);
    }
}
