package com.increff.pos.dao;

import com.increff.pos.entity.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class ProductDao extends AbstractDao<ProductPojo> {

    final String SELECT_BY_BARCODE = "select p from ProductPojo p where barCode=:barCode";

    public ProductPojo select(String barCode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BARCODE, ProductPojo.class);
        query.setParameter("barCode", barCode);
        return getSingle(query);
    }

    public void update(ProductPojo productPojo) {

    }

}
