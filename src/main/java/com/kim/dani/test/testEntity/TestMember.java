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
public class TestMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String testMemberName;

    //    @JoinColumn(name = "testCart")
    @OneToOne(mappedBy = "testMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private TestCart testCart;


    @OneToMany(mappedBy = "testMember",cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<TestProduct> testProducts = new ArrayList<>();

}
