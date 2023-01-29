package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class InventoryUpsertForm {

    @NotBlank(message = "Barcode can not be null or whitespace characters")
    private String barcode;

    @NotNull(message = "Product quantity can not be null")
    @Positive(message = "Product quantity must be greater than 0")
    private Integer quantity;
}
