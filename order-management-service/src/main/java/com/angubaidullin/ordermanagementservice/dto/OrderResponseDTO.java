package com.angubaidullin.ordermanagementservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
