package com.kim.dani.dtoSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListCustomerSetDto {

    private Long orderId;

    private String address;

    private String email;

    private String phone;

    private String productName;

    private String productPrice;

    private Long productQuantity;

    private String message;

}
