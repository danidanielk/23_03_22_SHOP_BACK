package com.kim.dani.dtoSet;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageSetDto {

    private Long productId;

    private Long cartProductId;

    private Long memberId;

    private String productName;

    private String productImage;

    private String productPrice;

    private String productContent;

    private Long productQuantity;

    private Long thisQuantity;

    private String category;




}
