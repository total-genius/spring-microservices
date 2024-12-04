package com.angubaidullin.ordermanagementservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String status;
    private String transactionId;
    private LocalDateTime timestamp;
}
