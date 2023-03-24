package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.ProductDetailSetDto;
import com.kim.dani.dtoSet.ProductListSetDto;
import com.kim.dani.dtoSet.ProductUploadSetDto;
import com.kim.dani.entity.*;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {



    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartAndProductRepository cartAndProductRepository;
    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QMember qMember = QMember.member;
    private final QCart qCart = QCart.cart;
    private final QCartAndProduct qCartAndProduct = QCartAndProduct.cartAndProduct;
    private final JwtTokenV2 jwtTokenV2;






    //카테고리에 맞는 상품 리스트
    public List<ProductListSetDto> productList(String category){

        List<ProductListSetDto> setDtoList = new ArrayList<>();
        List<Product> products = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.category.productCategory.eq(category))
                .fetch();

        for (Product product : products) {
        ProductListSetDto setDto = new ProductListSetDto(product.getId(),
                product.getProductName(), product.getProductImage(),
                product.getProductPrice(), product.getProductContent(),
                product.getProductQuantity(), product.getCategory().getProductCategory());
            setDtoList.add(setDto);
        }
        return setDtoList;
    }



    //상품 디테일
    public ProductDetailSetDto productDetail(Long productId){
        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();
        return new ProductDetailSetDto(product.getId(), product.getProductName(),
                product.getProductImage(), product.getProductPrice(),
                product.getProductContent(), product.getProductQuantity(),
                product.getCategory().getProductCategory());

    }

    //상품 ADD
    public void productAdd(Long productId , HttpServletRequest req) {
        String MemberEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req);

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();

        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.Email.eq(MemberEmail))
                .fetchOne();

        Cart cart = queryFactory
                .selectFrom(qCart)
                .where(qCart.member.eq(member))
                .fetchOne();

        if (cart == null){
            Cart cart1 = new Cart(null, member, null);
            cartRepository.save(cart1);
            CartAndProduct cartAndProduct = new CartAndProduct(null, cart1, product);
            cartAndProductRepository.save(cartAndProduct);
        }
        CartAndProduct cartAndProduct = new CartAndProduct(null, cart, product);
        cartAndProductRepository.save(cartAndProduct);

    }



}
