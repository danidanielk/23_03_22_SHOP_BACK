package com.kim.dani.test.testController;


import com.kim.dani.test.testEntity.*;
import com.kim.dani.test.testRepository.TestCartAndProductRepository;
import com.kim.dani.test.testRepository.TestCartRepository;
import com.kim.dani.test.testRepository.TestMemberRepository;
import com.kim.dani.test.testRepository.TestProductRepository;
import com.querydsl.codegen.QueryTypeFactory;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestControlloer {


    private final TestCartAndProductRepository testCartAndProductRepository;
    private final TestCartRepository testCartRepository;
    private final TestMemberRepository testMemberRepository;
    private final TestProductRepository testProductRepository;
    private final QTestCart qTestCart = QTestCart.testCart;
    private final QTestMember qTestMember = QTestMember.testMember;
    private final QTestProduct qTestProduct = QTestProduct.testProduct;
    private final QTestCartAndProduct qTestCartAndProduct = QTestCartAndProduct.testCartAndProduct;
    private final JPAQueryFactory jpaQueryFactory;

    // Manager 회원가입
    @GetMapping("test1")
    private void tesxt1() {
        TestMember testMember = new TestMember(null, "MANAGER", null, null);
        testMemberRepository.save(testMember);
    }


    // 매니저가 첫번째 상품등록
    @GetMapping("test2")
    private void test2() {

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("MANAGER"))
                .fetchOne();

        TestProduct testProduct = new TestProduct(null, "first", null, testMember);
        testProductRepository.save(testProduct);

    }


    // 매니저가 두번째 상품등록
    @GetMapping("test3")
    private void test3() {

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("MANAGER"))
                .fetchOne();

        TestProduct testProduct = new TestProduct(null, "second", null, testMember);
        testProductRepository.save(testProduct);


    }

    // daniel 유저 회원가입
    @GetMapping("test4")
    private void test4() {

        TestMember testMember = new TestMember(null, "daniel", null, null);
        testMemberRepository.save(testMember);

    }

    // daniel2 유저 회원가입
    @GetMapping("test5")
    private void test5() {

        TestMember testMember = new TestMember(null, "daniel2", null, null);
        testMemberRepository.save(testMember);
    }


    // 카트에 추가
    @GetMapping("test6")
    private void test6() {
        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel"))
                .fetchOne();


        TestCart testCart = new TestCart(null, null, testMember);

        testCartRepository.save(testCart);

        TestProduct testProduct = jpaQueryFactory
                .selectFrom(qTestProduct)
                .where(qTestProduct.productName.eq("first"))
                .fetchOne();

        TestCartAndProduct testCartAndProduct = new TestCartAndProduct(null, testCart, testProduct);
        testCartAndProductRepository.save(testCartAndProduct);
    }

    //daniel 의 카트 조회
    @GetMapping("test7")
    public ResponseEntity test7() {

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel"))
                .fetchOne();


        List<TestProduct> testProducts = jpaQueryFactory
                .select(qTestProduct)
                .from(qTestMember)
                .leftJoin(qTestMember.testCart, qTestCart)
                .leftJoin(qTestCart.testCartAndProducts, qTestCartAndProduct)
                .leftJoin(qTestCartAndProduct.testProduct, qTestProduct)
                .where(qTestMember.eq(testMember))
                .fetch();



        return new ResponseEntity(testProducts, HttpStatus.OK);

    }


    //daniel 이 카트에 등록했던 상품 또다시 추가.
    @GetMapping("test8")
    public ResponseEntity test8 (){

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel"))
                .fetchOne();

        TestProduct testProduct = jpaQueryFactory
                .selectFrom(qTestProduct)
                .where(qTestProduct.productName.eq("first"))
                .fetchOne();

        List<TestProduct> testProduct1 = jpaQueryFactory
                .select(qTestProduct)
                .from(qTestMember)
                .leftJoin(qTestMember.testCart, qTestCart)
                .leftJoin(qTestCart.testCartAndProducts, qTestCartAndProduct)
                .leftJoin(qTestCartAndProduct.testProduct, qTestProduct)
                .where(qTestMember.eq(testMember))
                .fetch();
        for (TestProduct product : testProduct1) {
            if(product.getProductName().equals(testProduct.getProductName())){
                return new ResponseEntity("The product already exists.", HttpStatus.BAD_REQUEST);
            }
        }
        TestCart testCart = new TestCart(null, null, testMember);
        testCartRepository.save(testCart);
        TestCartAndProduct testCartAndProduct = new TestCartAndProduct(null, testCart, testProduct);
        testCartAndProductRepository.save(testCartAndProduct);
        return new ResponseEntity(HttpStatus.OK);
    }

    //다시조회해보기
    @GetMapping("test9")
    public ResponseEntity test9(){

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel"))
                .fetchOne();

        List<TestProduct> testProducts = jpaQueryFactory
                .select(qTestProduct)
                .from(qTestMember)
                .leftJoin(qTestMember.testCart, qTestCart)
                .leftJoin(qTestCart.testCartAndProducts, qTestCartAndProduct)
                .leftJoin(qTestCartAndProduct.testProduct, qTestProduct)
                .where(qTestMember.eq(testMember))
                .fetch();

        return new ResponseEntity(testProducts, HttpStatus.OK);



    }

    //daniel이 second 상품 카트에 추가
    @GetMapping("test10")
    public void test10(){

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel"))
                .fetchOne();

        TestCart getTestCart = jpaQueryFactory
                .selectFrom(qTestCart)
                .where(qTestCart.testMember.eq(testMember))
                .fetchOne();

        TestProduct testProduct = jpaQueryFactory
                .selectFrom(qTestProduct)
                .where(qTestProduct.productName.eq("second"))
                .fetchOne();

        if(getTestCart == null){
        TestCart testCart = new TestCart(null, null, testMember);
        testCartRepository.save(testCart);
        TestCartAndProduct testCartAndProduct = new TestCartAndProduct(null, testCart, testProduct);
            testCartAndProductRepository.save(testCartAndProduct);
        }

        TestCartAndProduct testCartAndProduct = new TestCartAndProduct(null, getTestCart, testProduct);
        testCartAndProductRepository.save(testCartAndProduct);
    }

    //daniel 의 cart 조회하기 상품이 2개인지 확인.
    @GetMapping("test11")
    public ResponseEntity test11(){

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel"))
                .fetchOne();

        List<TestProduct> testProducts = jpaQueryFactory
                .select(qTestProduct)
                .from(qTestMember)
                .leftJoin(qTestMember.testCart, qTestCart)
                .leftJoin(qTestCart.testCartAndProducts, qTestCartAndProduct)
                .leftJoin(qTestCartAndProduct.testProduct, qTestProduct)
                .where(qTestMember.eq(testMember))
                .fetch();

        return new ResponseEntity(testProducts, HttpStatus.OK);


    }


    //daniel2 가 first 상품을 카트에 추가
    @GetMapping("test12")
    public void test12(){
        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel2"))
                .fetchOne();

        TestCart testCart = new TestCart(null, null, testMember);

        testCartRepository.save(testCart);

        TestProduct testProduct = jpaQueryFactory
                .selectFrom(qTestProduct)
                .where(qTestProduct.productName.eq("first"))
                .fetchOne();


        TestCartAndProduct testCartAndProduct = new TestCartAndProduct(null, testCart, testProduct);
        testCartAndProductRepository.save(testCartAndProduct);

    }

    //daniel2 의 cart 에  first 상품이 등록되었는지 조회해보기
    @GetMapping("test13")
    public ResponseEntity test13(){
        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel2"))
                .fetchOne();

        List<TestProduct> testProducts = jpaQueryFactory
                .select(qTestProduct)
                .from(qTestMember)
                .leftJoin(qTestMember.testCart, qTestCart)
                .leftJoin(qTestCart.testCartAndProducts, qTestCartAndProduct)
                .leftJoin(qTestCartAndProduct.testProduct, qTestProduct)
                .where(qTestMember.eq(testMember))
                .fetch();

        return new ResponseEntity(testProducts, HttpStatus.OK);
    }

    //daniel 의 cart 에 first 상품과 second 상품이 그대로 등록되어있는지 조회해보기
    @GetMapping("test14")
    public ResponseEntity test14(){
        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel"))
                .fetchOne();

        List<TestProduct> testProducts = jpaQueryFactory
                .select(qTestProduct)
                .from(qTestMember)
                .leftJoin(qTestMember.testCart, qTestCart)
                .leftJoin(qTestCart.testCartAndProducts, qTestCartAndProduct)
                .leftJoin(qTestCartAndProduct.testProduct, qTestProduct)
                .where(qTestMember.eq(testMember))
                .fetch();

        return new ResponseEntity(testProducts, HttpStatus.OK);
    }

    //daniel2에게 second 상품 등록
    @GetMapping("test15")
    public void test15(){

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel2"))
                .fetchOne();

        TestCart getTestCart = jpaQueryFactory
                .selectFrom(qTestCart)
                .where(qTestCart.testMember.eq(testMember))
                .fetchOne();

        TestProduct testProduct = jpaQueryFactory
                .selectFrom(qTestProduct)
                .where(qTestProduct.productName.eq("second"))
                .fetchOne();

        if (getTestCart == null) {
        TestCart testCart = new TestCart(null, null, testMember);
        testCartRepository.save(testCart);
        TestCartAndProduct testCartAndProduct = new TestCartAndProduct(null, testCart, testProduct);
        testCartAndProductRepository.save(testCartAndProduct);
        }

        TestCartAndProduct testCartAndProduct = new TestCartAndProduct(null, getTestCart, testProduct);
        testCartAndProductRepository.save(testCartAndProduct);
    }


    //daniel2 의 cart 조회
    @GetMapping("test16")
    public ResponseEntity test16 (){
        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("daniel2"))
                .fetchOne();

        List<TestProduct> testProducts = jpaQueryFactory
                .select(qTestProduct)
                .from(qTestMember)
                .leftJoin(qTestMember.testCart, qTestCart)
                .leftJoin(qTestCart.testCartAndProducts, qTestCartAndProduct)
                .leftJoin(qTestCartAndProduct.testProduct, qTestProduct)
                .where(qTestMember.eq(testMember))
                .fetch();

        return new ResponseEntity(testProducts, HttpStatus.OK);

    }


    //manager 의 등록한 상품 전체 조회
    @GetMapping("test17")
    public ResponseEntity test17() {

        TestMember testMember = jpaQueryFactory
                .selectFrom(qTestMember)
                .where(qTestMember.testMemberName.eq("MANAGER"))
                .fetchOne();

        List<TestProduct> testProducts = jpaQueryFactory
                .selectFrom(qTestProduct)
                .where(qTestProduct.testMember.eq(testMember))
                .fetch();

        return new ResponseEntity(testProducts, HttpStatus.OK);


    }




}
