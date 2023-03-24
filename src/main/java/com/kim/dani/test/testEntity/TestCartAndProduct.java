package com.kim.dani.test.testEntity;


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
public class TestCartAndProduct {


    @Id
    @GeneratedValue( strategy =  GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "testCart")
    private TestCart testCart;

    @ManyToOne
    @JoinColumn(name = "testProduct")
    private TestProduct testProduct;
}
