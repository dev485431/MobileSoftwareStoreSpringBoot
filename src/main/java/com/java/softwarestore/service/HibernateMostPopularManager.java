package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.dto.ProgramDetailsDto;
import com.java.softwarestore.model.dto.ProgramDetailsDtoFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HibernateMostPopularManager {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ProgramDetailsDtoFactory programDetailsDtoFactory;
    @Value("${popular.item.date.format}")
    private String popularItemDateFormat;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<ProgramDetailsDto> getTopPrograms(Integer limit, QueryResultsOrder downloadOrder, QueryResultsOrder
            timeUploadedOrder) {
        String query = "from Program p order by p.statistics.downloads " + downloadOrder.value() + ", p.statistics" +
                ".timeUploaded " + timeUploadedOrder.value();
        List<Program> programs = session().createQuery(query).setMaxResults(limit).setCacheable(true).list();

        programs.forEach(program -> {
            Hibernate.initialize(program.getCategory());
            Hibernate.initialize(program.getStatistics());
        });

        return programs.stream().map(program -> programDetailsDtoFactory.getDetailsDto(program,
                popularItemDateFormat)).collect(Collectors.toList());
    }
}
