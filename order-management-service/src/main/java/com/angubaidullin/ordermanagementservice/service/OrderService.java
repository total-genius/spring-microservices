package com.angubaidullin.ordermanagementservice.service;

import com.angubaidullin.ordermanagementservice.dto.OrderRequestDTO;
import com.angubaidullin.ordermanagementservice.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    OrderResponseDTO updateOrderStatus(Long id, String status);

    void deleteOrderById(Long id);
}
