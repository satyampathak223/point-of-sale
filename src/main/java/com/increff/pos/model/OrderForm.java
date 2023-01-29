package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    @NotEmpty(message = "Order items can not be null or empty")
    List<OrderItemForm> orderItemFormList;
}
