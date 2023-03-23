package com.kim.dani.test.testRepository;

import com.kim.dani.test.testEntity.TestProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestProductRepository extends JpaRepository<TestProduct, Long> {
}
