package com.increff.pos.dao;

import com.increff.pos.entity.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao<BrandPojo> {

    private final static String SELECT_USING_BRAND_AND_CATEGORY = "select p from BrandPojo p where (p.name=:name and p.category=:category)";

    @PersistenceContext
    private EntityManager entityManager;

    public BrandPojo getByBrandAndCategory(String name, String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_USING_BRAND_AND_CATEGORY, BrandPojo.class);
        query.setParameter("name", name);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<String> selectDistinctBrands() {
        TypedQuery<String> query = entityManager.createQuery("select DISTINCT(p.name) from " + className.getSimpleName() + " p", String.class);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    public void update(BrandPojo brandPojo) {

    }

}