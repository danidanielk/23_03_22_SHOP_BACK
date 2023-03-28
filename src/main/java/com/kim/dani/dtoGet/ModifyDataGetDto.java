package com.kim.dani.dtoGet;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyDataGetDto {

    private String phone;

    private String password;

    private String currentPassword;

}

