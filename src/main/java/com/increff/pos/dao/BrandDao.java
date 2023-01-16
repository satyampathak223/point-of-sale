package com.increff.pos.dao;

import com.increff.pos.entity.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class BrandDao extends AbstractDao<BrandPojo> {

    private static String select_using_brand_and_category = "select p from BrandPojo p where (p.name=:name and p.category=:category)";

    @PersistenceContext
    private EntityManager entityManager;

    public BrandPojo selectUsingBrandAndCategory(String name, String category) {
        TypedQuery<BrandPojo> query = getQuery(select_using_brand_and_category, BrandPojo.class);
        query.setParameter("name", name);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public void update(BrandPojo brandPojo) {

    }
}