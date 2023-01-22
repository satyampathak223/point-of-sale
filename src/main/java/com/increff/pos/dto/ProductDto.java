package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandServiceApi;
import com.increff.pos.api.ProductServiceApi;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.model.ProductUpsertForm;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductDto {

    @Autowired
    private ProductServiceApi productServiceApi;
    @Autowired
    private BrandServiceApi brandServiceApi;

    private ProductPojo convert(ProductUpsertForm productForm) throws ApiException {
        BrandPojo brandPojo = brandServiceApi.getByBrandAndCategory(productForm.getBrandName(), productForm.getCategory());

        if (brandPojo == null) {
            throw new ApiException("The brand category combination does not exist");
        }

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrandCategoryId(brandPojo.getId());
        productPojo.setName(productForm.getName());
        productPojo.setBarcode(productForm.getBarCode());
        productPojo.setMRP(productForm.getMrp());

        return productPojo;

    }

    public void checkNull(ProductUpsertForm productForm) throws ApiException {
        if (Objects.isNull(productForm)) {
            throw new ApiException("Product Form can not be null");
        }

        if (StringUtil.isEmpty(productForm.getBarCode())) {
            throw new ApiException("Product bar code can not be empty");
        }

        if (StringUtil.isEmpty(productForm.getName())) {
            throw new ApiException("Product name can not be empty");
        }

        if (StringUtil.isEmpty(productForm.getCategory())) {
            throw new ApiException("Product category can not be empty");
        }

        if (StringUtil.isEmpty(productForm.getMrp().toString())) {
            throw new ApiException("Product price can not be empty");
        }
    }

    //    TODO ask if convert barcode to lowercase or not
    protected void normalize(ProductUpsertForm productForm) {
        productForm.setName(StringUtil.toLowerCase(productForm.getName().trim()));
        productForm.setBarCode(StringUtil.toLowerCase(productForm.getBarCode()).trim());
    }

}
