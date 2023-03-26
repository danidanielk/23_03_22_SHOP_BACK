package com.kim.dani.dtoSet;


import com.kim.dani.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
public class ProductUploadSetDto {



    private String productName;

    private String productImage;

    private String productPrice;

    private String productContent;

    private Long productQuantity;

    private String category;
}

