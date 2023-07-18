package copm.zml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-14 17:37
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceGatewayMain8200 {
    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayMain8200.class, args);
    }
}
