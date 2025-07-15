package com.ayman.my_store_api.repositories;

import com.ayman.my_store_api.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}