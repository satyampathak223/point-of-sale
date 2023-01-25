//package com.increff.pos.api;
//
//import com.increff.pos.dao.InventoryDao;
//import com.increff.pos.entity.InventoryPojo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@Transactional(rollbackOn = ApiException.class)
//public class InventoryApi {
//    @Autowired
//    private InventoryDao inventoryDao;
//
//    public void add(InventoryPojo inventoryPojo) throws ApiException {
//        inventoryDao.insert(inventoryPojo);
//    }
//
//    public InventoryPojo get(Integer id) throws ApiException {
//        return getCheck(id);
//    }
//
//    public List<InventoryPojo> getAll() throws ApiException {
//        return inventoryDao.selectAll();
//    }
//
//    public InventoryPojo getCheck(Integer id) throws ApiException {
//        InventoryPojo inventoryPojo = inventoryDao.select(id);
//        if (Objects.isNull(inventoryPojo)) {
//            throw new ApiException("Inventory with given ID does not exist");
//        }
//        return inventoryPojo;
//    }
//
//    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
//        InventoryPojo previousInventoryPojo = getCheck(id);
//        previousInventoryPojo.setQuantity(inventoryPojo.getQuantity());
//        inventoryDao.update(inventoryPojo);
//    }
//
//}
