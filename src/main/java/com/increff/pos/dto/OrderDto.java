package com.increff.pos.dto;

import com.increff.pos.api.*;
import com.increff.pos.entity.InventoryPojo;
import com.increff.pos.entity.OrderItemPojo;
import com.increff.pos.entity.OrderPojo;
import com.increff.pos.model.*;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderDto extends AbstractDto<OrderForm> {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private OrderItemApi orderItemApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private OrderItemDto orderItemDto;

    public void checkNull(OrderForm orderForm) throws ApiException {
//        Check if order form is null or not
        checkNull(orderForm, "Order Form can not be null");

//        Check if orderFormItem is null or not
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            orderItemDto.checkNull(orderItemForm);
        }
    }

    public void checkForDuplicateBarCode(List<OrderItemForm> orderItemFormList) throws ApiException {
        HashSet<String> hashSet = new HashSet<>();
        List<String> duplicateBarCodes = new ArrayList<>();

        for (OrderItemForm orderItemForm : orderItemFormList) {
            String barcode = orderItemForm.getBarcode();
            if (hashSet.contains(barcode)) {
                duplicateBarCodes.add(barcode);
            }
            hashSet.add(barcode);
        }

        if (!CollectionUtils.isEmpty(duplicateBarCodes)) {
            throw new ApiException("Order items with duplicate barcode exist " + duplicateBarCodes.toString());
        }
    }

    public void checkIfPresentInInventory(List<OrderItemForm> orderItemFormList) throws ApiException {
        List<String> notAvailableInInventory = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemFormList) {

            InventoryPojo inventoryPojo = inventoryApi.getByBarCode(orderItemForm.getBarcode());

            if (Objects.isNull(inventoryPojo) || inventoryPojo.getQuantity() < orderItemForm.getQuantity()) {
                notAvailableInInventory.add(orderItemForm.getBarcode());
            }
        }
        if (!CollectionUtils.isEmpty(notAvailableInInventory))
            throw new ApiException("Product(s) with given barcode(s) not available. " + notAvailableInInventory.toString());

    }

    public void add(OrderForm orderForm) throws ApiException {
        validate(orderForm);
        normalize(orderForm);

        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();

        checkForDuplicateBarCode(orderForm.getOrderItemFormList());
        checkIfPresentInInventory(orderItemFormList);

        OrderPojo orderPojo = new OrderPojo();
        orderApi.add(orderPojo);

        Integer orderId = orderPojo.getId();
        for (OrderItemForm orderItemForm : orderItemFormList) {
            orderItemDto.add(orderItemDto.convert(orderId, orderItemForm));
        }
    }

    public void add(OrderPojo orderPojo) {
        orderApi.add(orderPojo);
    }

    public void addItem(Integer orderId, OrderItemForm orderItemForm) throws ApiException {
        if (Objects.isNull(orderApi.get(orderId))) {
            throw new ApiException(String.format("order with given id : %d dose not exist", orderId));
        }
        orderItemApi.add(orderItemDto.convert(orderId, orderItemForm));
    }

    public void updateItem(Integer id, OrderItemForm orderItemForm) {
        orderItemApi.update(id, orderItemForm);
    }

    public List<OrderData> get() {
        List<OrderPojo> pojos = orderApi.getAll();
        ArrayList<OrderData> datas = new ArrayList<>();
        for (OrderPojo pojo : pojos) {
            datas.add(convert(pojo));
        }
        return datas;
    }

    public List<OrderItemData> get(Integer orderId) throws ApiException {
        checkNull(orderApi.get(orderId), "Order with given ID does not exist");
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        List<OrderItemPojo> orderItemPojoList = orderItemApi.get(orderId);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemDataList.add(orderItemDto.convert(orderItemPojo));
        }
        return orderItemDataList;
    }

    public void getInvoice(Integer orderId) throws ApiException {
    }

    public InvoiceData getInvoiceDataByOrderPojo(OrderPojo orderPojo) throws ApiException {

    }

    public List<InvoiceItem> convert(List<OrderItemForm> orderItemFormList) {

    }

//        orderpojo to invoice data
//    list orderitemform to list of invoiceitem


    private OrderPojo convert(OrderForm orderForm) throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        Instant instant = Instant.now();
        return orderPojo;
    }

    private OrderData convert(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY, HH:mm:ss z", Locale.ENGLISH);
        orderData.setCreatedAt(orderPojo.getCreatedAt().format(dateTimeFormatter));
        return orderData;
    }

    private List<OrderItemData> convert(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderApi.get(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        if (Objects.isNull(orderPojo)) {
            throw new ApiException("Order with given ID does not exist");
        }

        List<OrderItemPojo> orderItemPojos = orderItemApi.get(orderId);

        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            orderItemDataList.add(orderItemDto.convert(orderItemPojo));
        }

        return orderItemDataList;
    }

    private void normalize(OrderForm orderForm) {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();

        for (OrderItemForm orderItemForm : orderItemFormList) {
            String barcode = StringUtil.toLowerCase(orderItemForm.getBarcode());
            orderItemForm.setBarcode(barcode);
        }
        orderForm.setOrderItemFormList(orderItemFormList);
    }

    @Override
    protected void validate(OrderForm orderForm) throws ApiException {
        super.validate(orderForm);

        for (OrderItemForm orderItemForm : orderForm.getOrderItemFormList()) {
            orderItemDto.validate(orderItemForm);

        }
    }
}
