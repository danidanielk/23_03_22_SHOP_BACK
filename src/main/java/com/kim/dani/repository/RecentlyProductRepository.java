package com.kim.dani.repository;

import com.kim.dani.entity.RecentlyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentlyProductRepository extends JpaRepository<RecentlyProduct,Long> {
}
