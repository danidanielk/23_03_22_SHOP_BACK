package com.kim.dani.controller;


import com.kim.dani.dtoSet.ProductDetailSetDto;
import com.kim.dani.dtoSet.ProductListAllSetDto;
import com.kim.dani.dtoSet.ProductListSetDto;
import com.kim.dani.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product관련 API.")
public class ProductController {

    private final ProductService productService;






    //카테고리에 맞는 상풍 리스트
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "success",content = @Content(schema = @Schema(implementation = ProductListSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")

    })
    @Operation(summary = "상품리스트",description = "카테고리와 일치하는 상품 리스트")
    @PostMapping("category/{category}")
    public ResponseEntity productList(@PathVariable String category){

        List<ProductListSetDto> setDto = productService.productList(category);
        if(setDto != null){
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //카테고리안의 상품 선택 시 상품 디테일 화면
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success",content = @Content(schema = @Schema(implementation = ProductDetailSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "상품 디테일",description = "상품선택시 상품 디테일 화면")
    @PostMapping("productid/{productId}")
    public ResponseEntity productDetail(@PathVariable Long productId,HttpServletRequest req,HttpServletResponse res){



        ProductDetailSetDto setDto = productService.productDetail(productId,req,res);
        if(setDto != null){
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //상품 Mypage에 추가 버튼
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "MyPage에 상품 추가")
    })
    @Operation(summary = "상품 추가",description = "Mypage에 상품 추가(장바구니)")
    @GetMapping("product/add/{productId}/{inQuantity}/{setPrice}")
    public ResponseEntity productAdd(@PathVariable Long productId ,
                                     @PathVariable Long inQuantity,
                                     @PathVariable Long setPrice,
                                     HttpServletRequest req, HttpServletResponse res){

        productService.productAdd(productId,inQuantity,setPrice, req,res);
        return new ResponseEntity(HttpStatus.OK);
        //commit
    }

    //전체 상품 list page
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "success",content = @Content(schema = @Schema(implementation = ProductListAllSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "전체상품",description = "전체상품 리스트")
    @PostMapping("listall")
    public ResponseEntity listAll() {
        List<ProductListAllSetDto> productListAllSetDtos = productService.listAll();
        if (productListAllSetDtos != null  ) {
            return new ResponseEntity(productListAllSetDtos, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }




}
