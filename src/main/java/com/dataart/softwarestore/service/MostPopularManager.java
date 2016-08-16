package com.dataart.softwarestore.service;

import com.dataart.softwarestore.model.dto.ProgramDetailsDto;

import java.util.List;

public interface MostPopularManager {

    List<ProgramDetailsDto> getTopPrograms(Integer limit, QueryResultsOrder downloadOrder, QueryResultsOrder timeUploadedOrder);

}
