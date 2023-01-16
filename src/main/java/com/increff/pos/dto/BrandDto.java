package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.model.BrandForm;


public class BrandDto {

    /**
     * Functions to validate if the input data is correct or not
     */
    public BrandPojo checkNotNull(BrandForm brandForm) throws ApiException {
        if (brandForm == null) {
            throw new ApiException("Brand name and category can not be null");
        }
        if (brandForm.getName() == null) {
            throw new ApiException("Brand name can not be null");
        }
        if (brandForm.getCategory() == null) {
            throw new ApiException("Brand category can not be null");
        }
        return null;
    }


}
