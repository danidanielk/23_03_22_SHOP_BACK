package com.kim.dani.test.testEntity;


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
@NoArgsConstructor
@AllArgsConstructor
public class TestMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String testMemberName;

    //    @JoinColumn(name = "testCart")
    @JsonIgnore
    @OneToOne(mappedBy = "testMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private TestCart testCart;

    @JsonIgnore
    @OneToMany(mappedBy = "testMember",cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<TestProduct> testProducts = new ArrayList<>();
}
