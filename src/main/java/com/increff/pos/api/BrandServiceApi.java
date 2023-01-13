package com.increff.pos.api;


import com.increff.pos.dao.BrandDao;
import com.increff.pos.entity.BrandPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn  = ApiException.class)
public class BrandServiceApi {

	@Autowired
	private BrandDao brandDao;
	public void add(BrandPojo brandPojo) throws ApiException {
		if(StringUtil.isEmpty(brandPojo.getName())) {
			throw new ApiException("Brand name cannot be empty");
		}

        if(StringUtil.isEmpty(brandPojo.getCategory())){
            throw new ApiException("Brand category cannot be empty");
        }

		normalize(brandPojo);

		if(brandDao.selectUsingBrandAndCategory(brandPojo.getName(),brandPojo.getCategory())!=null){
			throw new ApiException("Same combination of brand and category already exists");
		}
		brandDao.insert(brandPojo);
	}

	public void delete(int id) {
		brandDao.delete(id);
	}

	public BrandPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	public List<BrandPojo> getAll() {
		return brandDao.selectAll();
	}

	public BrandPojo getCheck(int id) throws ApiException {
		BrandPojo brandPojo = brandDao.select(id);
		if (brandPojo == null) {
			throw new ApiException("Employee with given ID does not exit, id: " + id);
		}
		return brandPojo;
	}


	public void update(int id, BrandPojo brandPojo) throws ApiException {
		normalize(brandPojo);
		if(brandDao.selectUsingBrandAndCategory(brandPojo.getName(),brandPojo.getCategory())!=null){
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
