package com.kim.dani.test.testRepository;


import com.kim.dani.test.testEntity.TestCartAndProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCartAndProductRepository extends JpaRepository<TestCartAndProduct,Long> {
}
