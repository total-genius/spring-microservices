package com.angubaidullin.ordermanagementservice.service.impl;

import com.angubaidullin.ordermanagementservice.dto.OrderRequestDTO;
import com.angubaidullin.ordermanagementservice.dto.OrderResponseDTO;
import com.angubaidullin.ordermanagementservice.dto.PaymentRequestDTO;
import com.angubaidullin.ordermanagementservice.dto.PaymentResponseDTO;
import com.angubaidullin.ordermanagementservice.entity.Order;
import com.angubaidullin.ordermanagementservice.entity.OrderStatus;
import com.angubaidullin.ordermanagementservice.entity.PaymentStatus;
import com.angubaidullin.ordermanagementservice.exception.OrderNotFoundException;
import com.angubaidullin.ordermanagementservice.feign.client.ChefClient;
import com.angubaidullin.ordermanagementservice.feign.client.PaymentClient;
import com.angubaidullin.ordermanagementservice.mapper.OrderMapper;
import com.angubaidullin.ordermanagementservice.repository.OrderRepository;
import com.angubaidullin.ordermanagementservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentClient paymentClient;
    private final ChefClient chefClient;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        // Создаем заказ
        Order order = orderMapper.toOrderEntity(orderRequestDTO);
        Order savedOrder = orderRepository.save(order);

        // Отправляем запрос в Payment Service для обработки платежа
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setOrderId(savedOrder.getId());
        paymentRequestDTO.setAmount(savedOrder.getTotalPrice());
        paymentRequestDTO.setPaymentMethod(orderRequestDTO.getPaymentMethod());

        PaymentResponseDTO paymentResponseDTO = paymentClient.processPayment(paymentRequestDTO);

        // Обновляем статус платежа в заказе
        savedOrder.setPaymentStatus(PaymentStatus.valueOf(paymentResponseDTO.getStatus()));
        savedOrder.setStatus(OrderStatus.PAID);
        orderRepository.save(savedOrder);

        // Уведомляем Chef Management Service о новом заказе
        chefClient.notifyNewOrder(savedOrder.getId());

        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return orderMapper.toOrderResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orderMapper.toOrderResponseDTOs(orders);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        return orderMapper.toOrderResponseDTO(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}
