package com.shoestore.product.repository;

import com.shoestore.product.entity.Shoe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ShoeRepository extends JpaRepository<Shoe,Long>, JpaSpecificationExecutor<Shoe> {
    List<Shoe> findByActiveTrue();

    Page<Shoe> findByActiveTrue(Pageable pageable);
}
