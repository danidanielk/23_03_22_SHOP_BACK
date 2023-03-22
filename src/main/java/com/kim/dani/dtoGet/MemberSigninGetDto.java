package com.kim.dani.dtoGet;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberSigninGetDto {

    @Schema(description = "email주소",example = "ex@naver.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "phone번호",example = "01011111111")
    @NotBlank
    private String phone;

    @Schema(description = "password",example = "숫자, 문자, 특수문자 포함 8~15자리 이내")
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",message = "숫자, 문자, 특수문자 포함 8~15자리 이내")
    @NotBlank
    private String password;

//    @Schema(description = "권한",example = "default로 CUSTOMER로 설정되어있음")
//    private Auth auth=Auth.CUSTOMER;
}
