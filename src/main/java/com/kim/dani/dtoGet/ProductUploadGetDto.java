package com.kim.dani.dtoGet;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUploadGetDto {


    @Schema(description = "상품 이름")
    @NotBlank
    private String productName;

    @Schema(description = "상품 가격")
    @NotBlank
    private String productPrice;

    @Schema(description = "상품 내용")
    @NotBlank
    private String productContent;

    @Schema(description = "재고 수량")
    private Long productQuentity;

    @Schema(description = "상품 카테고리 Category Entity")
    private String productCategory;

}
