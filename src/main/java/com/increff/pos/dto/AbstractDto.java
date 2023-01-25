package com.increff.pos.dto;

import com.increff.pos.api.ApiException;

import java.util.Objects;

public class AbstractDto {
    public void checkNull(Object object, String message) throws ApiException {
        if (Objects.isNull(object)) {
            throw new ApiException(message);
        }
    }

    /**
     * Tried to create generic function for converting pojo to data
     */
//    public List<T> pojoToData() {
//        return api.getAll().stream().map(ProductHelper::convert).collect(Collectors.toList());
//    }


}
