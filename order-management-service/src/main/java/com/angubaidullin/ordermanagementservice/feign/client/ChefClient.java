package com.angubaidullin.ordermanagementservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "chef-management-service")
public interface ChefClient {

    @PostMapping("/chefs/notifyOrder")
    void notifyNewOrder(@RequestParam Long orderId);
}

