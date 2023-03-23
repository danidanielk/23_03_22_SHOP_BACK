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
public class TestCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "testCart",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<TestCartAndProduct> testCartAndProducts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "testMember")
    private TestMember testMember;



}
