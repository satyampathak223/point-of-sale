package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductData extends ProductUpsertForm {
    @NotNull
    private Integer id;

}
