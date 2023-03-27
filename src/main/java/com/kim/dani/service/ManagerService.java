package com.kim.dani.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kim.dani.dtoGet.ProductPatchGetDto;
import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.OrderListManagerSetDto;
import com.kim.dani.dtoSet.ProductUploadSetDto;
import com.kim.dani.entity.*;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.BuyerRepository;
import com.kim.dani.repository.CategoryRepository;
import com.kim.dani.repository.ProductRepository;
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
public class ManagerService {


    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;


    private final AmazonS3Client amazonS3Client;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;
    private final QBuyer qBuyer = QBuyer.buyer;
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
                .where(qMember.email.eq(getEmail))
                .fetchOne();

        String path = photoS3Upload(file);

        Category category = new Category();
        category.setProductCategory(productUploadGetDto.getProductCategory());


        Product product = new Product(null,productUploadGetDto.getProductName(),
                path,
                productUploadGetDto.getProductPrice(),
                productUploadGetDto.getProductContent(),
                productUploadGetDto.getProductQuantity(),
                category,member );
        productRepository.save(product);


        return new ProductUploadSetDto(product.getProductName(),
                path,productUploadGetDto.getProductPrice(),
                productUploadGetDto.getProductContent(),
                productUploadGetDto.getProductQuantity(),
                productUploadGetDto.getProductCategory());
    }


    //상품 삭제
    public boolean delete(Long productId, HttpServletRequest req) {



        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req);

        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.email.eq(getEmail))
                .fetchOne();

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();

        if (member.getAuth().equals(Auth.MANAGER)) {
        productRepository.delete(product);
            return true;
        }
        return false;
    }


    //상품 수정
    public boolean patch(Long productId, HttpServletRequest req, ProductPatchGetDto productPatchGetDto,MultipartFile file) throws IOException {



        if (file != null) {
            String imagePath = photoS3Upload(file);
        } else {
        String imagePath = null;
        }
        String category = productPatchGetDto.getCategory();
        String content = productPatchGetDto.getProductContent();
        String price = productPatchGetDto.getProductPrice();
        String name = productPatchGetDto.getProductName();
        Long quentity = productPatchGetDto.getProductQuentity();

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req);

        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.email.eq(getEmail))
                .fetchOne();

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();

        if (member.getAuth().equals(Auth.MANAGER)) {
            if (file != null) {
                String imagePath = photoS3Upload(file);
            product.setProductImage(imagePath);
            product.setProductContent(category == null || category == ""?product.getCategory().getProductCategory() : category);
            product.setProductName(name == null || name == "" ? product.getProductName() : name);
            product.setProductPrice(price == null || price =="" ? product.getProductPrice() : price);
            product.setProductQuantity(quentity == null ? product.getProductQuantity() : quentity);
            product.setProductContent(content == null || content == "" ? product.getProductContent() : content);
            productRepository.save(product);
            return true;
            } else if (file == null) {
                product.setProductImage(product.getProductImage());
                product.setProductContent(category == null || category == ""?product.getCategory().getProductCategory() : category);
                product.setProductName(name == null || name == "" ? product.getProductName() : name);
                product.setProductPrice(price == null || price =="" ? product.getProductPrice() : price);
                product.setProductQuantity(quentity == null ? product.getProductQuantity() : quentity);
                product.setProductContent(content == null || content == "" ? product.getProductContent() : content);
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }

    //주문 내역
    public List<OrderListManagerSetDto> orderList(HttpServletRequest req) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req);

        System.out.println(getEmail+"4444444444444444");
        List<Buyer> buyer = queryFactory
                .selectFrom(qBuyer)
                .orderBy(qBuyer.id.desc())
                .fetch();

        List<OrderListManagerSetDto> orderListManagerSetDtos = new ArrayList<>();
        if (buyer != null) {
            for (Buyer buyer1 : buyer) {
                OrderListManagerSetDto setDto = new OrderListManagerSetDto();
                setDto.setOrderId(buyer1.getId());
                setDto.setAddress(buyer1.getAddress());
                setDto.setMessage(buyer1.getMessage());
                setDto.setEmail(buyer1.getEmail());
                setDto.setPhone(buyer1.getPhone());
                setDto.setProductPrice(buyer1.getProductPrice());
                setDto.setProductName(buyer1.getProductName());
                setDto.setProductQuantity(buyer1.getProductQuantity());
                orderListManagerSetDtos.add(setDto);
            }
            return orderListManagerSetDtos;
        }
        return null;
    }

}
