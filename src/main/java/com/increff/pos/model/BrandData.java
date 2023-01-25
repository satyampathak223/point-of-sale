package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BrandData extends BrandUpsertForm {
    @NotNull
    private Integer id;
}
