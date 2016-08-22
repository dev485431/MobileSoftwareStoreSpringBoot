package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.dto.ProgramDetailsDto;

public interface ProgramManager {

    void addProgram(Program program);

    boolean programNameExists(String name);

    Program getProgramById(Integer id);

    ProgramDetailsDto getProgramDetailsById(Integer id);

    void incrementDownloads(Integer programId);

    void removeProgram(Integer id);

}
