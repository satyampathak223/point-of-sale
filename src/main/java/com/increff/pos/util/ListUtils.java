package com.increff.pos.util;

import com.increff.pos.api.ApiException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ListUtils {

    public static void checkNonEmptyList(List<String> list, String message) throws ApiException {
        if (!CollectionUtils.isEmpty(list)) {
            throw new ApiException(message);
        }
    }

}
