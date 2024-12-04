package com.angubaidullin.ordermanagementservice.mapper;

import com.angubaidullin.ordermanagementservice.dto.OrderItemDTO;
import com.angubaidullin.ordermanagementservice.dto.OrderRequestDTO;
import com.angubaidullin.ordermanagementservice.dto.OrderResponseDTO;
import com.angubaidullin.ordermanagementservice.entity.Order;
import com.angubaidullin.ordermanagementservice.entity.OrderItem;
import com.angubaidullin.ordermanagementservice.entity.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OrderMapper {

    public Order toOrderEntity(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();

        order.setUserId(orderRequestDTO.getUserId());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        BigDecimal totalPrice = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();

        for (OrderItemDTO itemDTO : orderRequestDTO.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setOrder(order);

            BigDecimal itemTotalPrice = itemDTO.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalPrice = totalPrice.add(itemTotalPrice);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        return order;
    }

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setUserId(order.getUserId());
        orderResponseDTO.setTotalPrice(order.getTotalPrice());
        orderResponseDTO.setStatus(order.getStatus().name());
        orderResponseDTO.setPaymentStatus(order.getPaymentStatus().name());
        orderResponseDTO.setCreatedAt(order.getCreatedAt());

        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(item -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setProductId(item.getProductId());
                    orderItemDTO.setQuantity(item.getQuantity());
                    orderItemDTO.setPrice(item.getPrice());
                    return orderItemDTO;
                })
                .toList();
        orderResponseDTO.setItems(items);
        return orderResponseDTO;
    }

    public List<OrderResponseDTO> toOrderResponseDTOs(List<Order> orders) {
        return orders.stream()
                .map(this::toOrderResponseDTO)
                .toList();
    }
}
