package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryUpsertForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class InventoryApiController {
    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Add list of inventories")
    @RequestMapping(path = "/inventories", method = RequestMethod.POST)
    public void add(@RequestBody List<InventoryUpsertForm> inventoryForms) throws ApiException {
        inventoryDto.add(inventoryForms);
    }

    @ApiOperation(value = "Get a inventory by ID")
    @RequestMapping(path = "/inventories/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException {
        return inventoryDto.get(id);
    }


    @ApiOperation(value = "Get list of all inventories")
    @RequestMapping(path = "/inventories", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates a inventory")
    @RequestMapping(path = "/inventories/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody InventoryUpsertForm inventoryForm) throws ApiException {
        inventoryDto.update(inventoryForm);
    }


}
