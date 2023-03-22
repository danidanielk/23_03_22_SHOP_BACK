package com.kim.dani.entity;

import com.kim.dani.dtoGet.ProductUploadGetDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productCategory;

    private String image;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Product> products = new ArrayList<>();


}
