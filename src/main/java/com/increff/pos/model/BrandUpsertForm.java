package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BrandUpsertForm {
    @NotBlank
    private String name;
    @NotBlank
    private String category;
}
