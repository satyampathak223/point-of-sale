package com.increff.pos.api;


import com.increff.pos.dao.BrandDao;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandServiceApi extends AbstractApi {

    @Autowired
    private BrandDao brandDao;

    public void add(BrandPojo brandPojo) throws ApiException {
        if (StringUtil.isEmpty(brandPojo.getName())) {
            throw new ApiException("Brand name cannot be empty");
        }

        if (StringUtil.isEmpty(brandPojo.getCategory())) {
            throw new ApiException("Brand category cannot be empty");
        }

        normalize(brandPojo);

        if (brandDao.selectUsingBrandAndCategory(brandPojo.getName(), brandPojo.getCategory()) != null) {
            throw new ApiException("Same combination of brand and category already exists");
        }
        brandDao.insert(brandPojo);
    }

    public BrandPojo get(Integer id) throws ApiException {
        return getCheck(id);
    }

	public BrandPojo getByBrandAndCategory(String brand,String category){
		return brandDao.selectUsingBrandAndCategory(brand,category);
	}

    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    public BrandPojo getCheck(Integer id) throws ApiException {
        BrandPojo brandPojo = brandDao.select(id);
        if (brandPojo == null) {
            throw new ApiException("Brand with given ID does not exit, id: " + id);
        }
        return brandPojo;
    }


    public void update(Integer id, BrandPojo brandPojo) throws ApiException {
        normalize(brandPojo);
        if (brandDao.selectUsingBrandAndCategory(brandPojo.getName(), brandPojo.getCategory()) != null) {
            throw new ApiException("Same combination of brand and category already exists");
        }
        BrandPojo previousBrandPojo = getCheck(id);
        previousBrandPojo.setCategory(brandPojo.getCategory());
        previousBrandPojo.setName(brandPojo.getName());
        brandDao.update(previousBrandPojo);
    }

    protected static void normalize(BrandPojo brandPojo) {
        brandPojo.setName(StringUtil.toLowerCase(brandPojo.getName()).trim());
        brandPojo.setCategory(StringUtil.toLowerCase(brandPojo.getCategory()).trim());
    }

}
