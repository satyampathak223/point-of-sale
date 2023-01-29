package com.increff.pos.api;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.entity.InventoryPojo;
import com.increff.pos.entity.OrderItemPojo;
import com.increff.pos.model.OrderItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemApi {

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private InventoryApi inventoryApi;

    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(orderItemPojo.getProductId());
//      Get the number of quantity to be taken from order and update the inventory accordingly
        Integer remainingQuantity = inventoryApi.get(orderItemPojo.getProductId()).getQuantity() - orderItemPojo.getQuantity();
        inventoryPojo.setQuantity(remainingQuantity);
        inventoryApi.update(orderItemPojo.getProductId(), inventoryPojo);
        orderItemDao.insert(orderItemPojo);
    }

    public List<OrderItemPojo> get(Integer orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }

    public void update(Integer id, OrderItemForm orderItemForm) {

    }
}
