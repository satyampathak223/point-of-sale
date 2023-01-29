package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class OrderApiController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Creates an order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public void add(@RequestBody OrderForm orderForm) throws ApiException {
        orderDto.add(orderForm);
    }

    @ApiOperation(value = "adds an order item to the given orderId")
    @RequestMapping(path = "/orders/{orderId}", method = RequestMethod.POST)
    public void addItem(@PathVariable Integer orderId, @RequestBody OrderItemForm orderItemForm) throws ApiException {
        orderDto.addItem(orderId, orderItemForm);
    }

    @ApiOperation(value = "gets orderItems by orderId")
    @RequestMapping(path = "/orders/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> get(@PathVariable Integer orderId) throws ApiException {
        return orderDto.get(orderId);
    }

    @ApiOperation(value = "gets all orders")
    @RequestMapping(path = "/orders", method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException {
        return orderDto.get();
    }

    @ApiOperation(value = "get Invoice of an order by Id")
    @RequestMapping(path = "/get-invoice/{orderId}", method = RequestMethod.GET)
    public void update(@PathVariable Integer orderId) throws ApiException, IOException {
        orderDto.getInvoice(orderId);
    }

    //  TODO      Ask if we have to update an order because it doesn't makes any sense
    @ApiOperation(value = "Update an order item to the given orderId")
    @RequestMapping(path = "/orders/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody OrderItemForm orderItemForm) throws ApiException {
        orderDto.updateItem(id, orderItemForm);
    }
}
