package com.kim.dani.dtoSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListAllSetDto {

    private Long productId;

    private String productName;

    private String productImage;

    private String productPrice;

    private String productContent;

    private Long productQuantity;

    private String category;

}
