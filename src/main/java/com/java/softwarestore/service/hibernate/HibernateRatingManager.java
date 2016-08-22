package com.java.softwarestore.service.hibernate;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.domain.Rating;
import com.java.softwarestore.service.ProgramManager;
import com.java.softwarestore.service.RatingManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HibernateRatingManager implements RatingManager {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ProgramManager programManager;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional
    public void addRating(Integer programId, Rating rating) {
        Program program = programManager.getProgramById(programId);
        rating.setStatistics(program.getStatistics());
        program.getStatistics().getRatings().add(rating);
        session().save(program);
    }

}
