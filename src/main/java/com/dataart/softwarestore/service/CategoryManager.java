package com.dataart.softwarestore.service;

import com.dataart.softwarestore.model.domain.Category;

import java.util.List;

public interface CategoryManager {

    List<Category> getAllCategories();

    boolean categoryExists(String categoryName);

    Category getCategoryById(Integer id);

}
