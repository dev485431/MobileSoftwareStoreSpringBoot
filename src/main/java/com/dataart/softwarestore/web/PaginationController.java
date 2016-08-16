package com.dataart.softwarestore.web;

import com.dataart.softwarestore.model.dto.ProgramDetailsDto;
import com.dataart.softwarestore.service.PaginationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/programs")
public class PaginationController {

    @Autowired
    private PaginationManager paginationManager;

    @RequestMapping(value = "page/{pageNum:[\\d]+}", method = RequestMethod.GET)
    private List<ProgramDetailsDto> getPage(@PathVariable("pageNum") Integer pageNum, @RequestParam(value =
            "categoryId") Integer categoryId, @RequestParam(value = "itemsPerPage") Integer itemsPerPage) {

        return paginationManager.getPage(pageNum, categoryId, itemsPerPage);
    }

    @RequestMapping(value = "pages/max", method = RequestMethod.GET)
    private Integer getMaxPages(@RequestParam(value = "categoryId") Integer categoryId, @RequestParam(value =
            "itemsPerPage") Integer itemsPerPage) {
        return paginationManager.getMaxPageForCategory(categoryId, itemsPerPage);
    }

}
