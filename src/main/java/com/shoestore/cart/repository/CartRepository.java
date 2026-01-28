package com.shoestore.cart.repository;

import com.shoestore.auth.entity.User;
import com.shoestore.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUser(User user);
}
