package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandServiceApi;
import com.increff.pos.api.ProductServiceApi;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class ProductApiController {

    @Autowired
    private ProductServiceApi productServiceApi;
    @Autowired
    private BrandServiceApi brandServiceApi;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "/api/products", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm productForm) throws ApiException {
        ProductPojo productPojo = convert(productForm);
        productServiceApi.add(productPojo);
    }

    @ApiOperation(value = "Gets a product by ID")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Integer id) throws ApiException {
        ProductPojo productPojo = productServiceApi.get(id);
        return convert(productPojo);
    }

    @ApiOperation(value = "Gets list of all products")
    @RequestMapping(path = "/api/products", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException{
        List<ProductPojo> productPojoList = productServiceApi.getAll();
        List<ProductData> productDataList = new ArrayList<ProductData>();
        for (ProductPojo p : productPojoList) {
            productDataList.add(convert(p));
        }
        return productDataList;
    }

    @ApiOperation(value = "Updates a brand")
    @RequestMapping(path = "/api/products/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody ProductForm productForm) throws ApiException {
        ProductPojo productPojo = convert(productForm);
        productServiceApi.update(id, productPojo);
    }

    private ProductData convert(ProductPojo productPojo) throws ApiException{
        BrandPojo brandPojo = brandServiceApi.get(productPojo.getBrandCategoryId());
        ProductData productData = new ProductData();
        productData.setName(productPojo.getName());
        productData.setBrandName(brandPojo.getName());
        productData.setCategory(brandPojo.getCategory());
        productData.setBarCode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMRP());
        productData.setId(productPojo.getId());
        return productData;
    }

	private ProductPojo convert(ProductForm productForm) throws ApiException{
        BrandPojo brandPojo = brandServiceApi.getByBrandAndCategory(productForm.getBrandName(), productForm.getCategory());

        if(brandPojo==null){
            throw new ApiException("The brand category combination does not exist");
        }

        ProductPojo productPojo=new ProductPojo();

        Integer brandCategoryId=brandPojo.getId();

        productPojo.setBrandCategoryId(brandCategoryId);
        productPojo.setName(productForm.getName());
        productPojo.setBarcode(productForm.getBarCode());
        productPojo.setMRP(productForm.getMrp());

        return productPojo;
    }

}
