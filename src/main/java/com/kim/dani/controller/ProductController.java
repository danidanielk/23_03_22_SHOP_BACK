package com.kim.dani.controller;


import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.ProductDetailSetDto;
import com.kim.dani.dtoSet.ProductListSetDto;
import com.kim.dani.dtoSet.ProductUploadSetDto;
import com.kim.dani.entity.Product;
import com.kim.dani.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product관련 API.")
public class ProductController {

    private final ProductService productService;






    //카테고리에 맞는 상풍 리스트
    @ApiResponse(responseCode = "200",description = "상품 리스트",content = @Content(schema = @Schema(implementation = ProductListSetDto.class)))
    @Operation(summary = "상품리스트",description = "상품리스트")
    @PostMapping("category/{category}")
    public ResponseEntity productList(@PathVariable String category){

        List<ProductListSetDto> setDto = productService.productList(category);
        if(setDto != null){
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //카테고리안의 상품 선택 시 상품 디테일 화면
    @ApiResponse(responseCode = "200", description = "상품 디테일",content = @Content(schema = @Schema(implementation = ProductDetailSetDto.class)))
    @Operation(summary = "상품 디테일",description = "상품 디테일")
    @PostMapping("productid/{productId}")
    public ResponseEntity productDetail(@PathVariable Long productId){

        ProductDetailSetDto setDto = productService.productDetail(productId);
        if(setDto != null){
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //상품 Mypage에 추가 버튼
    @ApiResponse(responseCode = "200",description = "MyPage에 상품 추가")
    @Operation(summary = "상품 추가",description = "상품 추가")
    @GetMapping("product/add/{productId}")
    public ResponseEntity productAdd(@PathVariable Long productId, HttpServletRequest req){

        productService.productAdd(productId, req);
        return new ResponseEntity(HttpStatus.OK);
        //commit
    }



}
