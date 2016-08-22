package com.java.softwarestore.service;

import com.java.softwarestore.model.dto.ProgramDetailsDto;

import java.util.List;

public interface PaginationManager {

    List<ProgramDetailsDto> getPage(Integer pageNum, Integer categoryId, Integer itemsPerPage);

    Integer getMaxPageForCategory(Integer categoryId, Integer itemsPerPage);

}
