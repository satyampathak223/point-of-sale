package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InventoryUpsertForm {

    @NotNull
    @NotBlank
    private String barcode;

    @NotNull
    @NotBlank
    private Integer quantity;
}
