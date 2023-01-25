package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class InventoryUpsertForm {

    @NotBlank
    private String barcode;

    @NotNull
    @Positive
    private Integer quantity;
}
