package com.angubaidullin.ordermanagementservice.repository;

import com.angubaidullin.ordermanagementservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
