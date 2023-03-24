package com.kim.dani.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productName;

    @Column(length = 5000)
    private String productImage;

    private String productPrice;

    private String productContent;

    private Long productQuantity;

    @JoinColumn(name = "category_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartAndProduct> cartAndProduct = new ArrayList<>();
}
