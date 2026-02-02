package com.shoestore.order.repository;

import com.shoestore.auth.entity.User;
import com.shoestore.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
