package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HibernateCategoryManager {

    @Autowired
    private SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Category> getAllCategories() {
        return session().createQuery("from Category").setCacheable(true).list();
    }

    @Transactional(readOnly = true)
    public boolean categoryExists(String categoryName) {
        return getCategoryByName(categoryName) != null;
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Integer id) {
        return (Category) session().createQuery("from Category where id=:id").setParameter("id", id).uniqueResult();
    }

    private Category getCategoryByName(String categoryName) {
        return (Category) session().createQuery("from Category where name=:categoryName").setParameter
                ("categoryName", categoryName).uniqueResult();
    }

}
