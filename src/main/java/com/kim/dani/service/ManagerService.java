package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.ProductUploadSetDto;
import com.kim.dani.entity.*;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.CategoryRepository;
import com.kim.dani.repository.ProductRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ManagerService {


    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    private final AmazonS3Client amazonS3Client;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QMember qMember = QMember.member;
    private final JwtTokenV2 jwtTokenV2;





    //상품 업로드 S3 Get Path
    public String photoS3Upload(MultipartFile file) throws IOException {

        String path = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String uuidPath = "shop/" + path + uuid;
        String returnPath = "http://" + bucket + ".s3." + region + ".amazonaws.com/" + uuidPath;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3Client.putObject(bucket, uuidPath, file.getInputStream(), metadata);
        return returnPath;
    }


    //상품 업로드.
    public ProductUploadSetDto productUpload(ProductUploadGetDto productUploadGetDto, MultipartFile file, HttpServletRequest req) throws IOException {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req);
        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.Email.eq(getEmail))
                .fetchOne();

        String path = photoS3Upload(file);

        Category category = new Category();
        category.setProductCategory(productUploadGetDto.getProductCategory());
        categoryRepository.save(category);

        Product product = new Product(null,productUploadGetDto.getProductName(),
                path,
                productUploadGetDto.getProductPrice(),
                productUploadGetDto.getProductContent(),
                productUploadGetDto.getProductQuentity(),
                category,member ,null);
        productRepository.save(product);


        return new ProductUploadSetDto(product.getProductName(),
                path,productUploadGetDto.getProductPrice(),
                productUploadGetDto.getProductContent(),
                productUploadGetDto.getProductQuentity(),
                productUploadGetDto.getProductCategory());
    }

}
