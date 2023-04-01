package com.kim.dani.dtoSet;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkSet {


    private Long memberId;

    private Long productId;

    private String productImage;

    private String productName;

    private String productPrice;
}

