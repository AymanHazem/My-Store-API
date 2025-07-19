package com.ayman.my_store_api.orders;

import com.ayman.my_store_api.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o from Order o WHERE o .customer = :customer")
    List<Order> getOrdersByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o from Order o WHERE o .id = :orderId")
    Optional<Order> getOrderWithItems(@Param("orderId") Long orderId);
}