package com.kim.dani.dtoSet;


import com.kim.dani.entity.Auth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthSetDto {

    @Schema(description = "Auth enum. MANAGER / CUSTOMER")
    private Auth auth;

}
