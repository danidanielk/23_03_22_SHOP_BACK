package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.ProductDetailSetDto;
import com.kim.dani.dtoSet.ProductListAllSetDto;
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
    private final CartProductRepository cartProductRepository;
    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QMember qMember = QMember.member;
    private final QCart qCart = QCart.cart;
    private final QCartProduct qCartProduct = QCartProduct.cartProduct;
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
    public boolean productAdd(Long productId ,Long inQuantity,Long setPrice, HttpServletRequest req) {
        String MemberEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req);

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();

        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.email.eq(MemberEmail))
                .fetchOne();

        CartProduct cartProduct = new CartProduct(null,product.getId(), product.getProductName(), product.getProductImage(),
                String.valueOf(setPrice), product.getProductContent(), inQuantity,product.getCategory().getProductCategory(),member,null);

        cartProductRepository.save(cartProduct);

        Cart cart = queryFactory
                .selectFrom(qCart)
                .where(qCart.member.eq(member))
                .fetchOne();

        List<CartProduct> products = queryFactory
                .select(qCartProduct)
                .from(qMember)
                .leftJoin(qMember.cart, qCart)
                .leftJoin(qCart.cartAndProduct, qCartAndProduct)
                .leftJoin(qCartAndProduct.cartProduct, qCartProduct)
                .where(qMember.eq(member))
                .fetch();


        if (cart == null){
            Cart cart1 = new Cart(null, member, null);
            cartRepository.save(cart1);
            CartAndProduct cartAndProduct = new CartAndProduct(null, cart1, cartProduct);
            cartAndProductRepository.save(cartAndProduct);
            return true;
        }

//        for (Product product1 : products) {
//            if (product1.getId().equals(productId)) {
//                return false;
//            }
//        }

        CartAndProduct cartAndProduct = new CartAndProduct(null, cart, cartProduct);
        cartAndProductRepository.save(cartAndProduct);
        return true;

    }


    //전체상품 리스트
    public List<ProductListAllSetDto> listAll(){
        List<Product> products = queryFactory
                .selectFrom(qProduct)
                .fetch();

        List<ProductListAllSetDto> productListAllSetDtos = new ArrayList<>();

        for (Product product : products) {
            ProductListAllSetDto productListAllSetDto = new ProductListAllSetDto(product.getId(), product.getProductName(),
                    product.getProductImage(), product.getProductPrice(), product.getProductPrice(), product.getProductQuantity(),
                    product.getCategory().getProductCategory());
            productListAllSetDtos.add(productListAllSetDto);
        }
        return productListAllSetDtos;
    }



}
