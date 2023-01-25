package com.increff.pos.api;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.model.BrandUpsertForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandApi {
    //TODO always normalise on  api layer
    @Autowired
    private BrandDao brandDao;

    public void add(List<BrandPojo> brandPojos) throws ApiException {
        for (BrandPojo brandPojo : brandPojos) {
            brandDao.insert(brandPojo);
        }
    }

    public void checkIfAlreadyExists(List<BrandUpsertForm> brandForms) throws ApiException {
        List<String> errorMessage = new ArrayList<>();
        for (BrandUpsertForm brandForm : brandForms) {
            if (Objects.nonNull(getByBrandAndCategory(brandForm.getName(), brandForm.getCategory()))) {
                String erroneousMessage = brandForm.getName() + "_" + brandForm.getCategory();
                errorMessage.add(erroneousMessage);
            }
        }

        if (!errorMessage.isEmpty()) {
            throw new ApiException("Brand_category combination already exist. Erroneous combination\n" + errorMessage.toString());
        }
    }

    public BrandPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

    public BrandPojo getByBrandAndCategory(String brand, String category) {
        return brandDao.getByBrandAndCategory(brand, category);
    }

    public List<BrandPojo> getByBrandName(String name) {
        return brandDao.select("name", name);
    }

    public List<String> getDistinctBrands() {
        return brandDao.selectDistinctBrands();
    }

    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    public BrandPojo getCheck(Integer id) throws ApiException {
        BrandPojo brandPojo = brandDao.select(id);
        if (Objects.isNull(brandPojo)) {
            throw new ApiException("Brand with given ID does not exist, id: " + id);
        }
        return brandPojo;
    }

    public BrandPojo update(Integer id, BrandPojo brandPojo) throws ApiException {

        if (Objects.nonNull(brandDao.getByBrandAndCategory(brandPojo.getName(), brandPojo.getCategory()))) {
            throw new ApiException("Same combination of brand and category already exists");
        }
        BrandPojo previousBrandPojo = getCheck(id);
        previousBrandPojo.setCategory(brandPojo.getCategory());
        previousBrandPojo.setName(brandPojo.getName());
        brandDao.update(previousBrandPojo);

        return previousBrandPojo;
    }

}
