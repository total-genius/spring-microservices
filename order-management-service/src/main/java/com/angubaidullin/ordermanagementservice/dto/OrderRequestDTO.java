package com.angubaidullin.ordermanagementservice.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderRequestDTO {
    @NotNull
    private Long userId;
    @NotNull
    private List<OrderItemDTO> items;
    @NotNull
    private String paymentMethod;
    private String deliveryAddress;
}
