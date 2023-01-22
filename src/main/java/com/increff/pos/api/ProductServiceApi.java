package com.increff.pos.api;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.entity.ProductPojo;
import com.increff.pos.util.StringUtil;
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
        if (StringUtil.isEmpty(productPojo.getName())) {
            throw new ApiException("Product name cannot be empty");
        }

        if (StringUtil.isEmpty(productPojo.getBarcode())) {
            throw new ApiException("Product barcode cannot be empty");
        }

        normalize(productPojo);

//        if (brandDao.selectUsingBrandAndCategory(brandPojo.getName(), brandPojo.getCategory()) != null) {
//            throw new ApiException("Same combination of brand and category already exists");
//        }
        if(productDao.select(productPojo.getBarcode())!=null){
            throw new ApiException("Product with this Bar Code already exists");
        }
        productDao.insert(productPojo);
    }

    public ProductPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

//	public BrandPojo getByBrandAndCategory(String brand,String category){
//		return brandDao.selectUsingBrandAndCategory(brand,category);
//	}

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


    public void update(Integer id, ProductPojo productPojo) throws ApiException {
        normalize(productPojo);
//        if (productDao.select(productPojo.getBarcode()) != null) {
//            throw new ApiException("Product already exists");
//        }

        ProductPojo previousProductPojo = getCheck(id);
        previousProductPojo.setBrandCategoryId(productPojo.getBrandCategoryId());
        previousProductPojo.setBarcode(productPojo.getBarcode());
        previousProductPojo.setMRP(productPojo.getMRP());
        previousProductPojo.setName(productPojo.getName());

        productDao.update(previousProductPojo);
    }

    protected static void normalize(ProductPojo productPojo) {
        productPojo.setName(StringUtil.toLowerCase(productPojo.getName()).trim());
        productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()).trim());
    }

}
