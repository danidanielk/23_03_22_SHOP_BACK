package com.kim.dani.dtoSet;

import com.kim.dani.entity.Auth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberLoginSetDto {

    private String email;

    private Auth auth;

    private String accessToken;

    private String refreshToken;



}
