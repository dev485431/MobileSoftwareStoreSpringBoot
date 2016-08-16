package com.dataart.softwarestore.web;

import com.dataart.softwarestore.service.CategoryManager;
import com.dataart.softwarestore.service.PaginationManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    private static final Logger LOG = Logger.getLogger(MainController.class);
    private static final String MAIN_PAGE = "index";
    @Value("${pagination.default.page.number}")
    Integer defaultPageNumber;
    @Value("${pagination.default.category.id}")
    Integer defaultCategoryId;
    @Value("${pagination.default.items.per.page}")
    Integer defaultItemsPerPage;
    private CategoryManager categoryManager;
    private PaginationManager paginationManager;
    @Value("${pagination.items.per.page.options}")
    private int[] itemsPerPageOptions;
    @Value("${program.default.img128}")
    private String defaultImg128;

    @Autowired
    public MainController(CategoryManager categoryManager, PaginationManager paginationManager) {
        this.categoryManager = categoryManager;
        this.paginationManager = paginationManager;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    private String getMainPage(Model model, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               @RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage) {
        LOG.debug("Getting main page");
        model.addAttribute("allCategories", categoryManager.getAllCategories());
        if (pageNumber == null) pageNumber = defaultPageNumber;
        if (categoryId == null) categoryId = defaultCategoryId;
        if (itemsPerPage == null) itemsPerPage = defaultItemsPerPage;
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("itemsPerPageOptions", itemsPerPageOptions);
        model.addAttribute("maxPage", paginationManager.getMaxPageForCategory(categoryId, itemsPerPage));
        model.addAttribute("pageContent", paginationManager.getPage(pageNumber, categoryId, itemsPerPage));
        return MAIN_PAGE;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(Model model) {
        model.addAttribute("allCategories", categoryManager.getAllCategories());
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String getLogoutPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("allCategories", categoryManager.getAllCategories());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

}
