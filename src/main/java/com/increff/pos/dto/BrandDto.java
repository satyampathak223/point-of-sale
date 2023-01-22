package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandServiceApi;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandUpsertForm;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BrandDto {

    @Autowired
    private BrandServiceApi brandServiceApi;

    private BrandPojo convert(BrandUpsertForm brandForm) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setName(brandForm.getName());
        brandPojo.setCategory(brandForm.getCategory());
        return brandPojo;
    }

    private BrandData convert(BrandPojo brandPojo) {
        BrandData brandData = new BrandData();
        brandData.setName(brandPojo.getName());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    public void checkNull(BrandUpsertForm brandForm) throws ApiException {
        if (Objects.isNull(brandForm)) {
            throw new ApiException("Brand form can not be null");
        }

        if (StringUtil.isEmpty(brandForm.getName())) {
            throw new ApiException("Brand name can not be empty");
        }

        if (StringUtil.isEmpty(brandForm.getCategory())) {
            throw new ApiException("Brand category can not be empty");
        }
    }

    protected void normalize(BrandUpsertForm brandForm) {
        brandForm.setName(StringUtil.toLowerCase(brandForm.getName()).trim());
        brandForm.setCategory(StringUtil.toLowerCase(brandForm.getCategory()).trim());
    }

    public BrandData get(Integer id) throws ApiException {
        return convert(brandServiceApi.get(id));
    }

    public BrandData get(BrandUpsertForm brandUpsertForm) {
        return convert(brandServiceApi.getByBrandAndCategory(brandUpsertForm.getName(), brandUpsertForm.getCategory()));
    }

    //    Gets all categories present of a given brand required for dropdown menu
    public List<String> get(String brandName) {
        List<BrandPojo> brandPojos = brandServiceApi.getByBrandName(brandName);
        List<String> brands = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojos) {
            brands.add(brandPojo.getCategory());
        }
        return brands;
    }

    public List<String> getDistinctBrands() {
        return brandServiceApi.getDistinctBrands();
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandPojos = brandServiceApi.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojos) {
            brandDataList.add(convert(brandPojo));
        }
        return brandDataList;
    }

    //    TODO check for duplicates
    public void add(List<BrandUpsertForm> brandForms) throws ApiException {
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (BrandUpsertForm brandForm : brandForms) {
            checkNull(brandForm);
            normalize(brandForm);
        }

        /**
         * Checking for duplicates and generating error message according to it
         */
        String errorMessage = "";
        for (Integer i = 0; i < brandForms.size(); i++) {
            BrandUpsertForm form1 = brandForms.get(i);
            for (Integer j = i + 1; j < brandForms.size(); j++) {
                BrandUpsertForm form2 = brandForms.get(j);

//              Duplicate element exists
                if (form1.getName().equals(form2.getName()) && form1.getCategory().equals(form2.getCategory())) {
                    errorMessage += String.format("%s:%s in rows %d and %d", form1.getName(), form1.getCategory(), i + 1, j + 1);
                }
            }
        }

        if (!StringUtil.isEmpty(errorMessage)) {
            throw new ApiException("Duplicate combination of brand:category exists \n" + errorMessage);
        }

        for (BrandUpsertForm brandForm : brandForms) {
            brandPojos.add(convert(brandForm));
        }
        brandServiceApi.add(brandPojos);
    }

    public void update(Integer id, BrandUpsertForm brandForm) throws ApiException {
        checkNull(brandForm);
        normalize(brandForm);
        brandServiceApi.update(id, convert(brandForm));
    }

}
