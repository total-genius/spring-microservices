package com.angubaidullin.ordermanagementservice.controller;

import com.angubaidullin.ordermanagementservice.dto.OrderRequestDTO;
import com.angubaidullin.ordermanagementservice.dto.OrderResponseDTO;
import com.angubaidullin.ordermanagementservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Создание заказа
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO responseDTO = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    // Получение заказа по ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO responseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(responseDTO);
    }

    // Получение заказов пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // Обновление статуса заказа
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        OrderResponseDTO responseDTO = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(responseDTO);
    }

    // Удаление заказа
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}