package com.kim.dani.repository;

import com.kim.dani.entity.CartAndProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartAndProductRepository extends JpaRepository<CartAndProduct,Long> {
}
