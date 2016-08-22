package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Category;

import java.util.List;

public interface CategoryManager {

    List<Category> getAllCategories();

    boolean categoryExists(String categoryName);

    Category getCategoryById(Integer id);

}
