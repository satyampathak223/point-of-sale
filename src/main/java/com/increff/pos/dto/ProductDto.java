package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandServiceApi;
import com.increff.pos.api.ProductServiceApi;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductUpsertForm;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    private ProductData convert(ProductPojo productPojo) throws ApiException {
        BrandPojo brandPojo = brandServiceApi.get(productPojo.getBrandCategoryId());
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setBarCode(productPojo.getBarcode());
        productData.setBrandName(brandPojo.getName());
        productData.setCategory(brandPojo.getCategory());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMRP());

        return productData;

    }

    private List<ProductData> convert(List<ProductPojo> productPojos) throws ApiException {
        List<ProductData> productDatas = new ArrayList<>();

        for (ProductPojo productPojo : productPojos) {
            productDatas.add(convert(productPojo));
        }

        return productDatas;
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
    private void normalize(ProductUpsertForm productForm) {
        productForm.setBrandName(StringUtil.toLowerCase(productForm.getBrandName()).trim());
        productForm.setCategory(StringUtil.toLowerCase(productForm.getCategory()).trim());
        productForm.setName(StringUtil.toLowerCase(productForm.getName().trim()));
        productForm.setBarCode(StringUtil.toLowerCase(productForm.getBarCode()).trim());
    }

    public ProductData get(Integer id) throws ApiException {
        return convert(productServiceApi.get(id));
    }

    public List<ProductData> get() throws ApiException {
        return convert(productServiceApi.getAll());
    }

    public void update(ProductUpsertForm productForm) throws ApiException {
        checkNull(productForm);
        productServiceApi.update(productForm.getId(), convert(productForm));
    }

    public void add(ProductUpsertForm productForm) throws ApiException {
        normalize(productForm);
        checkNull(productForm);
        if (Objects.nonNull(productServiceApi.getByBarCode(productForm.getBarCode()))) {
            throw new ApiException("Barcode already exists \n" + productForm.getBarCode());
        }
        productServiceApi.add(convert(productForm));
    }

    public void add(List<ProductUpsertForm> productForms) throws ApiException {
        String errorMessage = "";
//      Check for duplicates in product form
        for (Integer i = 0; i < productForms.size(); i++) {
            ProductUpsertForm form1 = productForms.get(i);
            for (Integer j = i + 1; j < productForms.size(); j++) {
                ProductUpsertForm form2 = productForms.get(j);
                if (form1.getBarCode().equals(form2.getBarCode())) {
                    errorMessage += String.format("%s in rows %d and %d\n", form1.getBarCode(), i + 1, j + 1);
                }
            }
        }
        if (!StringUtil.isEmpty(errorMessage)) {
            throw new ApiException("Duplicate barcode exists \n" + errorMessage);
        }

        errorMessage = "";

//        Check if barcode is already inserted into database
        for (Integer i = 0; i < productForms.size(); i++) {
            ProductUpsertForm productUpsertForm = productForms.get(i);
            if (Objects.nonNull(productServiceApi.getByBarCode(productUpsertForm.getBarCode()))) {
                errorMessage += String.format("%s in row %d\n", productUpsertForm.getBarCode(), i + 1);
            }
        }

        if (!StringUtil.isEmpty(errorMessage)) {
            throw new ApiException("Barcode already exists \n" + errorMessage);
        }

        for (ProductUpsertForm productForm : productForms) {
            normalize(productForm);
            checkNull(productForm);
            add(productForm);
        }

    }

}
