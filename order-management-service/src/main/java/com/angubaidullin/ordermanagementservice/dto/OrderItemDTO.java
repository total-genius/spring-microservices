package com.angubaidullin.ordermanagementservice.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private BigDecimal price;
}
