package com.shoestore.product.repository;

import com.shoestore.product.entity.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoeRepository extends JpaRepository<Shoe,Long> {
    List<Shoe> findByActiveTrue();
}
