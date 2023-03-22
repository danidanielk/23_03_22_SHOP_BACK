package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.ProductDetailSetDto;
import com.kim.dani.dtoSet.ProductListSetDto;
import com.kim.dani.dtoSet.ProductUploadSetDto;
import com.kim.dani.entity.Category;
import com.kim.dani.entity.Product;
import com.kim.dani.entity.QProduct;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.CategoryRepository;
import com.kim.dani.repository.ProductRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {



    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final JwtTokenV2 jwtTokenV2;






    //상품 리스트
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



}
