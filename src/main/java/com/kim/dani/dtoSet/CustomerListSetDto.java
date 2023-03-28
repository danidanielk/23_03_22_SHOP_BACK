package com.kim.dani.dtoSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListSetDto {

    private String email;

    private String phone;

    private Long purchase;

    private String gradle;
}
