package com.increff.pos.api;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandServiceApi {

    @Autowired
    private BrandDao brandDao;

    public void add(BrandPojo brandPojo) throws ApiException {
        if (Objects.nonNull(getByBrandAndCategory(brandPojo.getName(), brandPojo.getCategory()))) {
            System.out.println("Duplicate brand category combination");
            return;
        }
        brandDao.insert(brandPojo);
    }

    public void add(List<BrandPojo> brandPojos) throws ApiException {
        String errorMessage = "";

        for (Integer i = 0; i < brandPojos.size(); i++) {
            BrandPojo brandPojo = brandPojos.get(i);

            for (Integer j = i + 1; j < brandPojos.size(); j++) {
                if (brandPojos.size() == 1) {
//               Single element so no need to display row number
                    errorMessage += String.format("%s:%s\n", brandPojo.getName(), brandPojo.getCategory());
                } else {
                    errorMessage += String.format("%s:%s in row %d\n", brandPojo.getName(), brandPojo.getCategory(), i + 1, j + 1);
                }
            }
        }
        if (!StringUtil.isEmpty(errorMessage)) {
            throw new ApiException("This brand:category combination already exists");
        }
        for (BrandPojo brandPojo : brandPojos) {
            add(brandPojo);
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
        if (brandPojo == null) {
            throw new ApiException("Brand with given ID does not exist, id: " + id);
        }
        return brandPojo;
    }

    public void update(Integer id, BrandPojo brandPojo) throws ApiException {

        if (Objects.nonNull(brandDao.getByBrandAndCategory(brandPojo.getName(), brandPojo.getCategory()))) {
            throw new ApiException("Same combination of brand and category already exists");
        }
        BrandPojo previousBrandPojo = getCheck(id);
        previousBrandPojo.setCategory(brandPojo.getCategory());
        previousBrandPojo.setName(brandPojo.getName());
        brandDao.update(previousBrandPojo);
    }

}
