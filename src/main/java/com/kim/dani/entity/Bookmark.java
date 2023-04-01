package com.kim.dani.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Bookmark {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "member")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "product")
    @ManyToOne
    private Product product;
}
