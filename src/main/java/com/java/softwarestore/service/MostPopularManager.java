package com.java.softwarestore.service;

import com.java.softwarestore.model.dto.ProgramDetailsDto;

import java.util.List;

public interface MostPopularManager {

    List<ProgramDetailsDto> getTopPrograms(Integer limit, QueryResultsOrder downloadOrder, QueryResultsOrder timeUploadedOrder);

}
