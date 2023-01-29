package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import com.increff.pos.entity.InventoryPojo;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryUpsertForm;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class InventoryDto extends AbstractDto<InventoryUpsertForm> {
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    public void checkNull(InventoryUpsertForm inventoryForm) throws ApiException {
        checkNull(inventoryForm, "Inventory form can not be null");

        if (StringUtil.isEmpty(inventoryForm.getBarcode())) {
            throw new ApiException("Barcode can not be null");
        }

        if (inventoryForm.getQuantity() <= 0) {
            throw new ApiException("Quantity can not be less than or equal to Zero");
        }
    }

    public InventoryData get(Integer id) throws ApiException {
        return convert(inventoryApi.get(id));
    }


    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojos = inventoryApi.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();

        for (InventoryPojo inventoryPojo : inventoryPojos) {
            inventoryDataList.add(convert(inventoryPojo));
        }
        return inventoryDataList;
    }

    public void checkForDuplicates(List<InventoryUpsertForm> inventoryForms) throws ApiException {
        HashSet<String> hashSet = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (InventoryUpsertForm inventoryForm : inventoryForms) {
            String barcode = inventoryForm.getBarcode();
            if (hashSet.contains(barcode)) {
                duplicates.add(barcode);
            }
            hashSet.add(inventoryForm.getBarcode());
        }

        if (!CollectionUtils.isEmpty(duplicates)) {
            throw new ApiException("Duplicate barcode exists in uploaded file. Erroneous entries \n" + duplicates.toString());
        }
    }

    public void checkIfPresentInProduct(List<InventoryUpsertForm> inventoryForms) throws ApiException {
        List<String> errorMessages = new ArrayList<>();

        for (InventoryUpsertForm inventoryForm : inventoryForms) {
            String barcode = inventoryForm.getBarcode();
            if (Objects.isNull(productApi.getByBarCode(barcode))) {
                errorMessages.add(barcode);
            }
        }

        if (!CollectionUtils.isEmpty(errorMessages)) {
            throw new ApiException("Product(s) does not exist in database. Erroneous barcode(s) \n" + errorMessages.toString());
        }
    }

    public void add(List<InventoryUpsertForm> inventoryForms) throws ApiException {
        for (InventoryUpsertForm inventoryForm : inventoryForms) {
            checkNull(inventoryForm);
            normalize(inventoryForm);
        }

//        Check if there are any duplicates in inventory form list sent by user
        checkForDuplicates(inventoryForms);

//        Check if barcode already exists in product table in database
        checkIfPresentInProduct(inventoryForms);

//        Check if a product with that barcode is already added to inventory or not
        inventoryApi.checkIfAlreadyExists(inventoryForms);

        List<InventoryPojo> inventoryPojos = new ArrayList<>();
        for (InventoryUpsertForm inventoryForm : inventoryForms) {
            inventoryPojos.add(convert(inventoryForm));
        }
        inventoryApi.add(inventoryPojos);
    }

    public void update(InventoryUpsertForm inventoryForm) throws ApiException {
        checkNull(inventoryForm);
        normalize(inventoryForm);
        Integer id = productApi.getByBarCode(inventoryForm.getBarcode()).getId();
        inventoryApi.update(id, convert(inventoryForm));
    }

    private InventoryPojo convert(InventoryUpsertForm inventoryForm) throws ApiException {
        ProductPojo productPojo = productApi.getByBarCode(inventoryForm.getBarcode());
        if (Objects.isNull(productPojo)) {
            throw new ApiException("Product with given barcode does not exist");
        }

        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }

    private InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
        InventoryData inventoryData = new InventoryData();
        ProductPojo productPojo = productApi.get(inventoryPojo.getProductId());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setProductId(productPojo.getId());
        inventoryData.setBarcode(productPojo.getBarcode());
        return inventoryData;
    }

    protected void normalize(InventoryUpsertForm inventoryForm) {
        inventoryForm.setBarcode(StringUtil.toLowerCase(inventoryForm.getBarcode()).trim());
    }
}
