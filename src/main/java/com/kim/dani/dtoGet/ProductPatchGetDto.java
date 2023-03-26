package com.kim.dani.dtoGet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPatchGetDto {

    @Schema
    private Long productId;

    @Schema(example = "String Product Name",description = "null 허용")
    @Nullable
    private String productName;

    @Schema(example = "String Product Image Path",description = "null 허용")
    @Nullable
    private String productImage;

    @Schema(example = "String Product Price",description = "null 허용")
    @Nullable
    private String productPrice;

    @Schema(example = "String Product Content",description = "null 허용")
    @Nullable
    private String productContent;

    @Schema(example = "Long 재고수량",description = "null 허용")
    @Nullable
    private Long productQuentity;

    @Schema(example = "String category",description = "null 허용")
    @Nullable
    private String category;
}
