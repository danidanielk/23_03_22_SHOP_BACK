package com.kim.dani.dtoSet;

import com.kim.dani.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuySetDto {


    private Long productId;

    private String productName;

    private String productImage;

    private String productPrice;

    private String productContent;

    private Long productQuantity;

    private String category;

    private Long memberId;

    private String email;

    private String phone;

}
