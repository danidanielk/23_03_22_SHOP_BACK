package com.kim.dani.test.testRepository;


import com.kim.dani.test.testEntity.TestMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestMemberRepository extends JpaRepository<TestMember,Long> {
}
