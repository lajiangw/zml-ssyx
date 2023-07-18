package com.zml;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 16:40
 */
@SpringBootApplication()
@EnableDiscoveryClient
//@ComponentScan("com")
//@MapperScan("com.zml.acl.mapper")
public class ServiceAciMain8201 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAciMain8201.class, args);
    }
}
