package com.kim.dani.dtoGet;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "String")
    private String address;

    @NotBlank
    @Schema(description = "String")
    private String email;

    @NotBlank
    @Schema(description = "String")
    private String phone;

    @NotBlank
    @Schema(description = "String")
    private String productName;

    @NotBlank
    @Schema(description = "String Type 가격 / NotBlank")
    private String productPrice;

    @NotNull
    @Schema(description = "Long Type 수량 / NotNull")
    private Long productQuantity;

    @Nullable
    @Schema(description = "String")
    private String message;
}
