package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandServiceApi;
import com.increff.pos.dto.BrandDto;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class BrandApiController {
    @Autowired
    private BrandServiceApi brandServiceApi;

    @ApiOperation(value = "Adds a brand")
    @RequestMapping(path = "/api/brands", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = convert(brandForm);
        brandServiceApi.add(brandPojo);
    }

    @ApiOperation(value = "Gets a brand by ID")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable Integer id) throws ApiException {
        BrandPojo brandPojo = brandServiceApi.get(id);
        return convert(brandPojo);
    }

	@ApiOperation(value = "Gets a brand by brand and category name")
    @RequestMapping(path = "/api/brands/{brand}/{category}", method = RequestMethod.GET)
    public BrandData getByBrandAndCategory(@PathVariable String brand, @PathVariable String category ) throws ApiException {
        BrandPojo brandPojo = brandServiceApi.getByBrandAndCategory(brand,category);
        return convert(brandPojo);
    }

    @ApiOperation(value = "Gets list of all brands")
    @RequestMapping(path = "/api/brands", method = RequestMethod.GET)
    public List<BrandData> getAll() {
        List<BrandPojo> brandPojoList = brandServiceApi.getAll();
        List<BrandData> brandDataList = new ArrayList<BrandData>();
        for (BrandPojo p : brandPojoList) {
            brandDataList.add(convert(p));
        }
        return brandDataList;
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody BrandForm brandForm) throws ApiException {
        BrandPojo brandPojo = convert(brandForm);
        brandServiceApi.update(id, brandPojo);
    }

    private static BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setName(brandPojo.getName());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

	private static BrandPojo convert(BrandForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setName(brandForm.getName());
        brandPojo.setCategory(brandForm.getCategory());
        return brandPojo;
    }
}