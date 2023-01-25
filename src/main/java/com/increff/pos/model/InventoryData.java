package com.increff.pos.model;

import javax.validation.constraints.NotNull;

public class InventoryData extends InventoryUpsertForm {
    @NotNull
    private Integer id;
}
