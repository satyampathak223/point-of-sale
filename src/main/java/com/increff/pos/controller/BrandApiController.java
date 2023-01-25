package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandUpsertForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/api")
public class BrandApiController {
    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "Add list of brand")
    @RequestMapping(path = "/brands", method = RequestMethod.POST)
    public void add(@RequestBody List<BrandUpsertForm> brandForms) throws ApiException {
        brandDto.add(brandForms);
    }

    @ApiOperation(value = "Get a brand by ID")
    @RequestMapping(path = "/brands/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        return brandDto.get(id);
    }

    @ApiOperation(value = "Get a brand by brand and category")
    @RequestMapping(path = "/get-by-brand-and-category", method = RequestMethod.POST)
    public BrandData getByBrandAndCategory(@RequestBody BrandUpsertForm brandForm) {
        return brandDto.get(brandForm);
    }

    @ApiOperation(value = "Get list of all brands")
    @RequestMapping(path = "/brands", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Get categories by brand")
    @RequestMapping(path = "/categories-by-brand/{name}", method = RequestMethod.GET)
    public List<String> getCategories(@PathVariable String name) {
        return brandDto.get(name);
    }

    @ApiOperation(value = "Get distinct brand names")
    @RequestMapping(path = "/distinct-brand", method = RequestMethod.GET)
    public List<String> getDistinctBrands() {
        return brandDto.getDistinctBrands();
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/brands/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody BrandUpsertForm brandForm) throws ApiException {
        brandDto.update(id, brandForm);
    }
}
//TODO pagination ( at last )
// TODO limit no of rows to be uploaded at once to 5000 as given in pos