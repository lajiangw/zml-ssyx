package com.zml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-12 16:19
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消自动配置mysql
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceSearchMain8204 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSearchMain8204.class, args);
    }
}
