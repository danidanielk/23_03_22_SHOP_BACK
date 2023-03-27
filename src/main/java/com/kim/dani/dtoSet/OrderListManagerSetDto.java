package com.kim.dani.dtoSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListManagerSetDto extends OrderListCustomerSetDto {

    private Long orderId;

    private String address;

    private String email;

    private String phone;

    private String productName;

    private String productPrice;

    private Long productQuantity;

    private String message;

}
