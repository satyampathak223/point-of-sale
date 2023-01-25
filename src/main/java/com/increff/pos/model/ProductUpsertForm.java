package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ProductUpsertForm {
    @NotBlank
    String barCode;
    @NotBlank
    String brandName;
    @NotBlank
    String category;
    @NotBlank
    String name;
    @NotNull
    @PositiveOrZero
    Double mrp;

}
