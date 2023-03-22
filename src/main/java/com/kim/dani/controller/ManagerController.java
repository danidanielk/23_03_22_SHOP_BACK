package com.kim.dani.controller;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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




}
