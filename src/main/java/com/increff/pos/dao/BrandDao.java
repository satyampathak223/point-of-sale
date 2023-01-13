package com.increff.pos.dao;

import com.increff.pos.entity.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao{

    private static String delete_id = "delete from BrandPojo p where id=:id";
	private static String select_id = "select p from BrandPojo p where id=:id";
    private static String select_all = "select p from BrandPojo p";
    private static String select_using_brand_and_category ="select p from BrandPojo p where (p.name=:name and p.category=:category)";

    @PersistenceContext
    private EntityManager entityManager;

    public void insert(BrandPojo brandPojo){
        entityManager.persist(brandPojo);
    }

    //   TODO remove this functionality when deploying in production
    public int delete(int id) {
		Query query = entityManager.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

    public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

    public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}

    public BrandPojo selectUsingBrandAndCategory(String name,String category){
        TypedQuery<BrandPojo> query=getQuery(select_using_brand_and_category, BrandPojo.class);
        query.setParameter("name", name);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public void update(BrandPojo brandPojo) {

	}
}
