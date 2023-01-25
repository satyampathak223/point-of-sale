package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandUpsertForm;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class BrandDto extends AbstractDto {

    @Autowired
    private BrandApi brandApi;

    public void checkNull(BrandUpsertForm brandForm) throws ApiException {

        checkNull(brandForm, "Brand form can not be null");

        if (StringUtil.isEmpty(brandForm.getName())) {
            throw new ApiException("Brand name can not be empty");
        }

        if (StringUtil.isEmpty(brandForm.getCategory())) {
            throw new ApiException("Brand category can not be empty");
        }
    }

    public BrandData get(Integer id) throws ApiException {
        return convert(brandApi.get(id));
    }

    public BrandData get(BrandUpsertForm brandUpsertForm) {
        return convert(brandApi.getByBrandAndCategory(brandUpsertForm.getName(), brandUpsertForm.getCategory()));
    }

    //    Gets all categories present of a given brand required for dropdown menu
    public List<String> get(String brandName) {
        List<BrandPojo> brandPojos = brandApi.getByBrandName(brandName);
        List<String> brands = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojos) {
            brands.add(brandPojo.getCategory());
        }
        return brands;
    }

    public List<String> getDistinctBrands() {
        return brandApi.getDistinctBrands();
    }

    public List<BrandData> getAll() {
        List<BrandPojo> brandPojos = brandApi.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for (BrandPojo brandPojo : brandPojos) {
            brandDataList.add(convert(brandPojo));
        }
        return brandDataList;
    }

    public void checkForDuplicates(List<BrandUpsertForm> brandForms) throws ApiException {
        String errorMessage = "";
        HashSet<BrandUpsertForm> brandUpsertFormSet = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (BrandUpsertForm brandForm : brandForms) {
            if (brandUpsertFormSet.contains(brandForm)) {
                String combination = brandForm.getName() + "_" + brandForm.getCategory();
                duplicates.add(combination);
            }
            brandUpsertFormSet.add(brandForm);
        }

        if (!duplicates.isEmpty()) {
            throw new ApiException("Duplicate Brand_Category exists. Erroneous combinations \n" + duplicates.toString());
        }
    }

    public void add(List<BrandUpsertForm> brandForms) throws ApiException {
        for (BrandUpsertForm brandForm : brandForms) {
            checkNull(brandForm);
            normalize(brandForm);
        }

        /**
         * Checking for duplicates and generating error message according to it
         */
        checkForDuplicates(brandForms);

//        Check if brand category combination already exists in database
        brandApi.checkIfAlreadyExists(brandForms);
        
        List<BrandPojo> brandPojos = new ArrayList<>();
        for (BrandUpsertForm brandForm : brandForms) {
            brandPojos.add(convert(brandForm));
        }
        brandApi.add(brandPojos);
    }

    public void update(Integer id, BrandUpsertForm brandForm) throws ApiException {
        checkNull(brandForm);
        normalize(brandForm);
        brandApi.update(id, convert(brandForm));
    }

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

    protected void normalize(BrandUpsertForm brandForm) {
        brandForm.setName(StringUtil.toLowerCase(brandForm.getName()).trim());
        brandForm.setCategory(StringUtil.toLowerCase(brandForm.getCategory()).trim());
    }

}
