package com.kim.dani.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String address;

    private String email;

    private String phone;

    private String productName;

    private String productPrice;

    private Long productQuantity;

    private String message;

    @ManyToOne
    @JoinColumn(name = "Member")
    private Member member;
}
