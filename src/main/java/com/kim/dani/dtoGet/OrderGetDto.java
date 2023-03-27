package com.kim.dani.dtoGet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderGetDto {

    @NotBlank
    private String address;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String productName;

    @NotBlank
    private String productPrice;

    @NotNull
    private Long productQuantity;

    @Nullable
    private String message;
}
