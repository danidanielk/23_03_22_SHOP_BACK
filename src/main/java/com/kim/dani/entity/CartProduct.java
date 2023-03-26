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
public class CartProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long productId;

    private String productName;

    @Column(length = 5000)
    private String productImage;

    private String productPrice;

    private String productContent;

    private Long productQuantity;

    private String category;

    @JoinColumn(name = "member")
    @ManyToOne
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "cartProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartAndProduct> cartAndProduct = new ArrayList<>();

}
