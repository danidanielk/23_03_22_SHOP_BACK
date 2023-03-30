package com.kim.dani.controller;


import com.kim.dani.dtoGet.ProductPatchGetDto;
import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.CustomerListSetDto;
import com.kim.dani.dtoSet.OrderListManagerSetDto;
import com.kim.dani.dtoSet.ProductUploadSetDto;
import com.kim.dani.service.ManagerService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("manager")
@Tag(name = "manager", description = "관리자 API.")
public class ManagerController {

    private final ProductService productService;
    private final ManagerService managerService;


    //상품 등록
    @ApiResponses(value ={
    @ApiResponse(responseCode = "200",description = "상품등록",content = @Content(schema = @Schema(implementation = ProductUploadSetDto.class))),
    @ApiResponse(responseCode = "400",description = "error code")})
    @Operation(summary = "상품등록",description = "상품등록")
    @PostMapping("/upload")
    public ResponseEntity productUpload(@Valid
                                        @RequestPart("formData") ProductUploadGetDto productUploadGetDto,
                                        @RequestPart("productImage") MultipartFile file,
                                        HttpServletRequest req, HttpServletResponse res) throws IOException {

        ProductUploadSetDto setDto = managerService.productUpload(productUploadGetDto,file,req,res);
        if (setDto !=null){
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    //상품 삭제
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제"),
            @ApiResponse(responseCode = "404", description = "error")
    })
    @Operation(summary =  "상품삭제",description = "상품삭제")
    @DeleteMapping("/delete/{productid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity delete(@PathVariable Long productid,HttpServletRequest req, HttpServletResponse res) {
        boolean answer = managerService.delete(productid, req,res);
        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //상품 수정
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "상품 수정"),
            @ApiResponse(responseCode = "404",description = "error")
    })
    @Operation(summary = "상품수정",description = "상품수정")
    @PatchMapping("/patch/{productid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity patch(@PathVariable Long productid,HttpServletRequest req,
                                @Valid @RequestPart("formData") ProductPatchGetDto productPatchGetDto,
                                @RequestParam(value = "productImage",required = false) MultipartFile file, HttpServletResponse res) throws IOException {
        boolean answer = managerService.patch(productid, req,productPatchGetDto,file,res);
        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //주문 내역
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "주문내역",content = @Content(schema = @Schema(implementation = OrderListManagerSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "주문내역")
    @GetMapping("/orderlist")
    public ResponseEntity orderList(HttpServletRequest req, HttpServletResponse res) {

        List<OrderListManagerSetDto> setDto = managerService.orderList(req,res);
        if (setDto != null) {
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //회원 리스트
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "회원리스트",content = @Content(schema = @Schema(implementation = CustomerListSetDto.class))),
            @ApiResponse(responseCode = "404",description = "error code")
    })
    @Operation(summary = "회원 리스트")
    @GetMapping("/customer/list")
    public ResponseEntity customerList(HttpServletRequest req) {

        List<CustomerListSetDto> setDtos = managerService.customerList(req);

        if (setDtos != null) {
            return new ResponseEntity(setDtos, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }




}
