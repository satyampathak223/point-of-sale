package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
public abstract class AbstractDao<T> {

    Class<T> className;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractDao() {
        this.className = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    protected <T> T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
        return entityManager.createQuery(jpql, clazz);
    }

    protected EntityManager em() {
        return entityManager;
    }

    public void insert(T objectPojo) {
        entityManager.persist(objectPojo);
    }

    public T select(Integer id) {
        final String SELECT_BY_ID = "select p from " + className.getSimpleName() + " p where id=:id";
        TypedQuery<T> query = getQuery(SELECT_BY_ID, className);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<T> selectAll() {
        final String SELECT_ALL = "select p from " + className.getSimpleName() + " p";
        TypedQuery<T> query = getQuery(SELECT_ALL, className);
        return query.getResultList();
    }

    public List<T> select(String key, String value) {
        final String SELECT_BY_KEY = "select p from " + className.getSimpleName() + " p where " + key + " = :value";
        TypedQuery<T> query = getQuery(SELECT_BY_KEY, className);
        query.setParameter("value", value);
        return query.getResultList();
    }

}