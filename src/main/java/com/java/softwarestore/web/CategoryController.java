package com.java.softwarestore.web;

import com.java.softwarestore.model.domain.Category;
import com.java.softwarestore.service.HibernateCategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/categories")
public class CategoryController {

    @Autowired
    private HibernateCategoryManager categoryManager;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    private List<Category> getAllCategories() {
        return categoryManager.getAllCategories();
    }

}
