package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.OrderItemApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.entity.OrderItemPojo;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderItemDto extends AbstractDto<OrderItemForm> {

    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    public void checkNull(OrderItemForm orderItemForm) throws ApiException {
        checkNull(orderItemForm, "Order item form can not be null");

        if (StringUtil.isEmpty(orderItemForm.getBarcode())) {
            throw new ApiException("Barcode can not be empty");
        }

        if (orderItemForm.getQuantity() <= 0) {
            throw new ApiException("Quantity can not be a negative number");
        }
    }

    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        orderItemApi.add(orderItemPojo);
    }

    public OrderItemPojo convert(Integer orderId, OrderItemForm orderItemForm) throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo();

        ProductPojo productPojo = productApi.getByBarCode(orderItemForm.getBarcode());

        if (Objects.isNull(productPojo)) {
            throw new ApiException("Product with given barcode is not available");
        }

        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setSellingPrice(productPojo.getMRP());
        return orderItemPojo;
    }

    public OrderItemData convert(OrderItemPojo orderItemPojo) throws ApiException {
        ProductPojo productPojo = productApi.get(orderItemPojo.getProductId());

        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setMrp(productPojo.getMRP());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setProductName(productPojo.getName());
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setBarcode(productPojo.getBarcode());

        return orderItemData;
    }


}
