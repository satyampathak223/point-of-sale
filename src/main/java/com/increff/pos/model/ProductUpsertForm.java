package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ProductUpsertForm {
    @NotBlank(message = "Barcode can not be null or whitespace characters")
    String barCode;
    @NotBlank(message = "Brand name can not be null or whitespace characters")
    String brandName;
    @NotBlank(message = "Brand category can not be null or whitespace character")
    String category;
    @NotBlank(message = "Brand category can not be null or whitespace character")
    String name;
    @NotNull(message = "Product MRP can not be null")
    @PositiveOrZero(message = "Product MRP must be greater than or equal to zero")
    Double mrp;

}
