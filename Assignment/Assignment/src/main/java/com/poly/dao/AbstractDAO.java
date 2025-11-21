package com.poly.dao;

import java.util.List;

import com.poly.util.JpaUtil;

import jakarta.persistence.*;

public abstract class AbstractDAO<T> {
    protected EntityManager em;

    public AbstractDAO() {
        this.em = JpaUtil.getEntityManager();
    }

    public T create(T entity) {
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(entity);
            trans.commit();
            return entity;
        } catch (Exception e) {
            trans.rollback();
            throw new RuntimeException(e);
        }
    }

    public T update(T entity) {
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(entity);
            trans.commit();
            return entity;
        } catch (Exception e) {
            trans.rollback();
            throw new RuntimeException(e);
        }
    }

    public T delete(T entity) {
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            trans.commit();
            return entity;
        } catch (Exception e) {
            trans.rollback();
            throw new RuntimeException(e);
        }
    }

    public T findById(Class<T> clazz, Object id) {
        return em.find(clazz, id);
    }

    public List<T> findAll(Class<T> clazz) {
        // THÊM DÒNG NÀY: Xóa cache để ép Hibernate lấy dữ liệu mới từ DB
        em.clear(); 
        
        String jsql = "SELECT o FROM " + clazz.getSimpleName() + " o";
        TypedQuery<T> query = em.createQuery(jsql, clazz);
        return query.getResultList();
    }
    
    // Bạn có thể thêm các phương thức chung khác (findAll có phân trang,...)
}