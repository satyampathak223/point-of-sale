package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class OrderItemForm {
    @NotNull(message = "Barcode can not be empty")
    @NotBlank(message = "Barcode can not be empty")
    private String barcode;

    @NotNull(message = "Quantity can not be null")
    @Positive(message = "Quantity must be a positive integer")
    private Integer quantity;
}
