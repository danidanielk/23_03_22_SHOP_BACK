package com.kim.dani.dtoSet;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecentlySet {

    private Long memberId;

    private Long productId;

    private String prodcutImage;

    private String productName;

    private String productPrice;

}
