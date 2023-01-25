package com.increff.pos.api;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.entity.InventoryPojo;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.model.InventoryUpsertForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryApi {
    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ProductApi productApi;

    public void add(List<InventoryPojo> inventoryPojos) {
        for (InventoryPojo inventoryPojo : inventoryPojos) {
            inventoryDao.insert(inventoryPojo);
        }
    }

    public void checkIfAlreadyExists(List<InventoryUpsertForm> inventoryForms) throws ApiException {
        List<String> existingBarCodes = new ArrayList<>();
        List<ProductPojo> productPojos = productApi.getAll();
        HashSet<String> productBarcodeHashSet = new HashSet<>();

        for (ProductPojo productPojo : productPojos) {
            productBarcodeHashSet.add(productPojo.getBarcode());
        }

        for (InventoryUpsertForm inventoryForm : inventoryForms) {
            if (productBarcodeHashSet.contains(inventoryForm.getBarcode())) {
                existingBarCodes.add(inventoryForm.getBarcode());
            }
        }
        if (!CollectionUtils.isEmpty(existingBarCodes)) {
            throw new ApiException("Item already present in inventory . Erroneous entry\n" + existingBarCodes.toString());
        }
    }

    public InventoryPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    public List<InventoryPojo> getAll() throws ApiException {
        return inventoryDao.selectAll();
    }

    public InventoryPojo getCheck(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(id);
        if (Objects.isNull(inventoryPojo)) {
            throw new ApiException("Inventory with given ID does not exist");
        }
        return inventoryPojo;
    }

    public void update(int id, InventoryPojo inventoryPojo) throws ApiException {

        InventoryPojo previousInventoryPojo = getCheck(id);

        previousInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }
}
