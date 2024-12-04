package com.angubaidullin.ordermanagementservice.repository;

import com.angubaidullin.ordermanagementservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
