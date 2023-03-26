package com.kim.dani.controller;


import com.kim.dani.dtoGet.ProductPatchGetDto;
import com.kim.dani.dtoGet.ProductUploadGetDto;
import com.kim.dani.dtoSet.ProductUploadSetDto;
import com.kim.dani.service.ManagerService;
import com.kim.dani.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("manager")
@Tag(name = "manager", description = "관리자 API.")
public class ManagerController {

    private final ProductService productService;
    private final ManagerService managerService;


    //상품 등록
    @ApiResponse(responseCode = "200",description = "상품등록",content = @Content(schema = @Schema(implementation = ProductUploadGetDto.class)))
    @Operation(summary = "상품등록",description = "상품등록")
    @PostMapping("/upload")
    public ResponseEntity productUpload(@Valid
                                        @RequestPart("formData") ProductUploadGetDto productUploadGetDto,
                                        @RequestPart("productImage") MultipartFile file, HttpServletRequest req) throws IOException {

        ProductUploadSetDto setDto = managerService.productUpload(productUploadGetDto,file,req);
        if (setDto !=null){
            return new ResponseEntity(setDto, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    //상품 삭제
    @ApiResponse(responseCode = "200", description = "상품 삭제")
    @Operation(summary =  "상품삭제",description = "상품삭제")
    @DeleteMapping("/delete/{productid}")
    @PreAuthorize("authenticated()")
    public ResponseEntity delete(@PathVariable Long productid,HttpServletRequest req) {
        boolean answer = managerService.delete(productid, req);
        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    //상품 수정
    @ApiResponse(responseCode = "200",description = "상품 수정",content = @Content(schema = @Schema(implementation = ProductPatchGetDto.class)))
    @Operation(summary = "상품수정",description = "상품수정")
    @PatchMapping("/patch/{productid}")
    @PreAuthorize("authenticated()")
    public ResponseEntity patch(@PathVariable Long productid,HttpServletRequest req,
                                @Valid @RequestPart("formData") ProductPatchGetDto productPatchGetDto,
                                @RequestParam(value = "productImage",required = false) MultipartFile file) throws IOException {
        boolean answer = managerService.patch(productid, req,productPatchGetDto,file);
        if (answer) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
