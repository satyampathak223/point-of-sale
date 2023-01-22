package com.increff.pos.api;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.entity.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductServiceApi {

    @Autowired
    private ProductDao productDao;

    public void add(ProductPojo productPojo) throws ApiException {
        productDao.insert(productPojo);
    }

    public ProductPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    public List<ProductPojo> getAll() {
        return productDao.selectAll();
    }

    public ProductPojo getCheck(Integer id) throws ApiException {
        ProductPojo productPojo = productDao.select(id);
        if (productPojo == null) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return productPojo;
    }

    /*
     * if the product is updated there are two cases
     * barcode is also changed
     * barcode remains the same
     */
    public void update(Integer id, ProductPojo productPojo) throws ApiException {

        ProductPojo previousProductPojo = getCheck(id);

        if (!previousProductPojo.getBarcode().equals(productPojo.getBarcode()) && !productDao.select("barcode", productPojo.getBarcode()).isEmpty()) {
            throw new ApiException(String.format("This barcode %s already exists", productPojo.getBarcode()));
        }

        previousProductPojo.setBrandCategoryId(productPojo.getBrandCategoryId());
        previousProductPojo.setBarcode(productPojo.getBarcode());
        previousProductPojo.setName(productPojo.getName());
        previousProductPojo.setMRP(productPojo.getMRP());

        productDao.update(previousProductPojo);
    }

    public ProductPojo getByBarCode(String barCode) throws ApiException {
        List<ProductPojo> productPojos = productDao.select("barcode", barCode);
        if (productPojos.isEmpty()) {
            return null;
        }
        return productPojos.get(0);
    }

}
