package com.kim.dani.dtoSet;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenSetDto {


    private String accessToken;
    private String refreshToken;
}
