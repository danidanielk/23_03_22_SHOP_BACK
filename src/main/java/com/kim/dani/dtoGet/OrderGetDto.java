package com.kim.dani.dtoGet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderGetDto {
    @NotBlank
    private String address;

    @NotBlank
    private String Email;

    @NotBlank
    private String phone;

    @NotBlank
    private String productName;

    @NotBlank
    private String productPrice;

    @NotBlank
    private Long productQuantity;

    @Nullable
    private String message;
}
