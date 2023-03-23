package com.kim.dani.test.testRepository;

import com.kim.dani.test.testEntity.TestCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCartRepository extends JpaRepository<TestCart, Long> {
}
