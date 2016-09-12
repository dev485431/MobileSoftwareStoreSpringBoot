package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.dto.ProgramDetailsDto;
import com.java.softwarestore.model.dto.ProgramDetailsDtoFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HibernateProgramManager {

    private static final int DOWNLOADS_INCREMENT = 1;
    private static final int ONE = 1;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ProgramDetailsDtoFactory programDetailsDtoFactory;
    @Value("${program.details.date.format}")
    private String programDetailsDateFormat;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public void addProgram(Program program) {
        session().save(program);
    }

    @Transactional(readOnly = true)
    public boolean programNameExists(String name) {
        Long count = (Long) session().createQuery("select count(*) from Program where name=:name").setParameter
                ("name", name).uniqueResult();
        return count == ONE;
    }

    @Transactional(readOnly = true)
    public Program getProgramById(Integer id) {
        return (Program) session().createQuery("from Program where id=:id").setParameter("id", id).uniqueResult();
    }

    @Transactional(readOnly = true)
    public ProgramDetailsDto getProgramDetailsById(Integer id) {
        Program program = (Program) session().createQuery("from Program where id=:id").setParameter("id", id)
                .uniqueResult();
        return programDetailsDtoFactory.getDetailsDto(program, programDetailsDateFormat);
    }

    @Transactional
    public void incrementDownloads(Integer programId) {
        Program program = getProgramById(programId);
        program.getStatistics().setDownloads(program.getStatistics().getDownloads() + DOWNLOADS_INCREMENT);
        session().update(program);
    }

    @Transactional
    public void removeProgram(Integer id) {
        Program program = getProgramById(id);
        session().delete(program);
    }

}
