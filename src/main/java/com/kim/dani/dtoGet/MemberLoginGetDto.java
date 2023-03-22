package com.kim.dani.dtoGet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberLoginGetDto {

    @Schema(description = "email주소",example = "ex@naver.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "password",example = "숫자, 문자, 특수문자 포함 8~15자리 이내")
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",message = "숫자, 문자, 특수문자 포함 8~15자리 이내")
    @NotBlank
    private String password;
}
