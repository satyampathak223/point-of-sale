package com.increff.pos.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ProductUpsertForm {
    @NonNull
    String barCode;
    @NonNull
    String brandName;
    @NonNull
    String category;
    @NonNull
    String name;
    @NonNull
    Double mrp;

}
