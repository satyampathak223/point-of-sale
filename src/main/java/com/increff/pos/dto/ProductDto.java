package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductUpsertForm;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class ProductDto extends AbstractDto {

    @Autowired
    private ProductApi productApi;
    @Autowired
    private BrandApi brandApi;

    public void checkNull(ProductUpsertForm productForm) throws ApiException {
        checkNull(productForm, "Product Form can not be null");

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

    public ProductData get(Integer id) throws ApiException {
        return convert(productApi.get(id));
    }

    public List<ProductData> get() throws ApiException {
        return convert(productApi.getAll());
    }

    public void update(Integer id, ProductUpsertForm productForm) throws ApiException {
        checkNull(productForm);
        productApi.update(id, convert(productForm));
    }

    public void add(ProductUpsertForm productForm) throws ApiException {
        normalize(productForm);
        checkNull(productForm);
        if (Objects.nonNull(productApi.getByBarCode(productForm.getBarCode()))) {
            throw new ApiException("Barcode already exists \n" + productForm.getBarCode());
        }
        productApi.add(convert(productForm));
    }

    public void checkForDuplicates(List<ProductUpsertForm> productForms) throws ApiException {
        HashSet<ProductUpsertForm> productUpsertFormSet = new HashSet<>();
        List<String> duplicateBarCodes = new ArrayList<>();

        for (ProductUpsertForm productForm : productForms) {
            if (productUpsertFormSet.contains(productForm)) {
                String combination = productForm.getBarCode();
                duplicateBarCodes.add(combination);
            }
            productUpsertFormSet.add(productForm);
        }

        if (!duplicateBarCodes.isEmpty()) {
            throw new ApiException("Duplicate products exists. Erroneous entries \n" + duplicateBarCodes.toString());
        }
    }

    public void add(List<ProductUpsertForm> productForms) throws ApiException {
        for (ProductUpsertForm productForm : productForms) {
            checkNull(productForm);
            normalize(productForm);
        }

        checkForDuplicates(productForms);

        productApi.checkIfAlreadyExists(productForms);

        List<ProductPojo> productPojos = new ArrayList<>();

        for (ProductUpsertForm productForm : productForms) {
            productPojos.add(convert(productForm));
        }

        productApi.add(productPojos);

    }

    private void normalize(ProductUpsertForm productForm) {
        productForm.setBrandName(StringUtil.toLowerCase(productForm.getBrandName()).trim());
        productForm.setCategory(StringUtil.toLowerCase(productForm.getCategory()).trim());
        productForm.setName(StringUtil.toLowerCase(productForm.getName().trim()));
        productForm.setBarCode(StringUtil.toLowerCase(productForm.getBarCode()).trim());
    }

    private ProductPojo convert(ProductUpsertForm productForm) throws ApiException {
        BrandPojo brandPojo = brandApi.getByBrandAndCategory(productForm.getBrandName(), productForm.getCategory());
        checkNull(brandPojo, "The brand category combination does not exist");
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrandCategoryId(brandPojo.getId());
        productPojo.setName(productForm.getName());
        productPojo.setBarcode(productForm.getBarCode());
        productPojo.setMRP(productForm.getMrp());

        return productPojo;

    }

    private ProductData convert(ProductPojo productPojo) throws ApiException {
        BrandPojo brandPojo = brandApi.get(productPojo.getBrandCategoryId());
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
}
