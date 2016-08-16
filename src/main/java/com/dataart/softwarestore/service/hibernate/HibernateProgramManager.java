package com.dataart.softwarestore.service.hibernate;

import com.dataart.softwarestore.model.domain.Program;
import com.dataart.softwarestore.model.dto.ProgramDetailsDto;
import com.dataart.softwarestore.service.ProgramManager;
import com.dataart.softwarestore.utils.ImageUrlType;
import com.dataart.softwarestore.utils.RatingsHandler;
import com.dataart.softwarestore.utils.UrlsHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
public class HibernateProgramManager implements ProgramManager {

    private static final int DOWNLOADS_INCREMENT = 1;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private UrlsHandler urlsHandler;
    @Autowired
    private RatingsHandler ratingsHandler;
    @Value("${program.details.date.format}")
    private String programDetailsDateFormat;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional
    public void addProgram(Program program) {
        session().save(program);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean programNameExists(String name) {
        Long count = (Long) session().createQuery("select count(*) from Program where name=:name").setParameter
                ("name", name).uniqueResult();
        return count == 1;
    }

    @Override
    @Transactional(readOnly = true)
    public Program getProgramById(Integer id) {
        return (Program) session().createQuery("from Program where id=:id").setParameter("id", id).uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramDetailsDto getProgramDetailsById(Integer id) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(programDetailsDateFormat);
        Program program = (Program) session().createQuery("from Program where id=:id").setParameter("id", id)
                .uniqueResult();
        return new ProgramDetailsDto(program.getId(), program.getName(),
                program.getDescription(),
                urlsHandler.getImageUrl(program, ImageUrlType.IMAGE_128),
                urlsHandler.getImageUrl(program, ImageUrlType.IMAGE_512),
                program.getCategory().getName(),
                program.getStatistics().getTimeUploaded().format(dateFormat),
                program.getStatistics().getDownloads(),
                ratingsHandler.getAverageRating(program.getStatistics().getRatings()));
    }

    @Override
    @Transactional
    public void incrementDownloads(Integer programId) {
        Program program = getProgramById(programId);
        program.getStatistics().setDownloads(program.getStatistics().getDownloads() + DOWNLOADS_INCREMENT);
        session().update(program);
    }

    @Override
    @Transactional
    public void removeProgram(Integer id) {
        Program program = getProgramById(id);
        session().delete(program);
    }

}
