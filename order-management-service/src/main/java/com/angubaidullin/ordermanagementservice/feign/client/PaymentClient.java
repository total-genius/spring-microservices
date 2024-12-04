package com.angubaidullin.ordermanagementservice.feign.client;

import com.angubaidullin.ordermanagementservice.dto.PaymentRequestDTO;
import com.angubaidullin.ordermanagementservice.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    PaymentResponseDTO processPayment(@RequestBody PaymentRequestDTO paymentRequestDTO);
}
