package com.kim.dani.dtoSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyDataSetDto {

    private Long memberId;



    private String email;

    private String phone;
}

