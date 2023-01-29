package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BrandUpsertForm {
    @NotBlank(message = "Brand name can not be null or whitespace characters")
    private String name;
    @NotBlank(message = "Brand category can not be null or whitespace characters")
    private String category;
}
