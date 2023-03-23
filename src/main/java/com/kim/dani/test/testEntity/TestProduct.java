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
@AllArgsConstructor
@NoArgsConstructor
public class TestProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productName;

    @OneToMany(mappedBy = "testProduct" , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<TestCartAndProduct> TestCartAndProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "testMember")
    private TestMember testMember;

}
