package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.domain.Rating;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HibernateRatingManager {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private HibernateProgramManager programManager;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public void addRating(Integer programId, Rating rating) {
        Program program = programManager.getProgramById(programId);
        rating.setStatistics(program.getStatistics());
        program.getStatistics().getRatings().add(rating);
        session().save(program);
    }

    @Transactional(readOnly = true)
    public Optional<Double> getAverageRating(Integer programId) {
        Program program = programManager.getProgramById(programId);
        return Optional.ofNullable((Double) session().createQuery("Select avg (rating.rating) from Rating rating " +
                "where " + "statistics_id=:statistics_id").setParameter("statistics_id", program.getStatistics()
                .getId()).uniqueResult());
    }

}
