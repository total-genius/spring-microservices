package com.angubaidullin.ordermanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OrderManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementServiceApplication.class, args);
    }

}
