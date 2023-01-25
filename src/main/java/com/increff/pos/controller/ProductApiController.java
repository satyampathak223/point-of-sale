package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.ProductDto;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductUpsertForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class ProductApiController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "/products", method = RequestMethod.POST)
    public void add(@RequestBody List<ProductUpsertForm> productForms) throws ApiException {
        productDto.add(productForms);
    }

    @ApiOperation(value = "Gets a product by ID")
    @RequestMapping(path = "/products/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id) throws ApiException {
        return productDto.get(id);
    }

    @ApiOperation(value = "Gets list of all products")
    @RequestMapping(path = "/products", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return productDto.get();
    }

    @ApiOperation(value = "Updates a Product")
    @RequestMapping(path = "/products/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductUpsertForm productForm) throws ApiException {
        productDto.update(id, productForm);
    }


}
