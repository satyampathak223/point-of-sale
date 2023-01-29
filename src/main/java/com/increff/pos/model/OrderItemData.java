package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {
    @NotNull
    private Integer id;

    @NotBlank(message = "Product name can not be empty")
    private String productName;

    @PositiveOrZero(message = "MRP must be a non negative integer")
    private Double mrp;
}
