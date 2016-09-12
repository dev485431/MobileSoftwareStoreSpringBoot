package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.dto.ProgramDetailsDto;
import com.java.softwarestore.model.dto.ProgramDetailsDtoFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HibernatePaginationManager {

    private static final int DECREMENT_BY_ONE = 1;
    private static final int MIN_PAGE_NO = 0;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ProgramDetailsDtoFactory programDetailsDtoFactory;
    @Value("${pagination.item.date.format}")
    private String paginationItemDateFormat;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<ProgramDetailsDto> getPage(Integer pageNum, Integer categoryId, Integer itemsPerPage) {
        Integer firstResult = pageNum * itemsPerPage;
        Criteria criteria = session().createCriteria(Program.class);
        List<Program> programs = criteria.add(Restrictions.eq("category.id", categoryId))
                .setCacheable(true)
                .setFirstResult(firstResult)
                .setMaxResults(itemsPerPage).list();

        programs.forEach(program -> {
            Hibernate.initialize(program.getCategory());
            Hibernate.initialize(program.getStatistics());
        });

        return programs.stream().map(program -> programDetailsDtoFactory.getDetailsDto(program,
                paginationItemDateFormat)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getMaxPageForCategory(Integer categoryId, Integer itemsPerPage) {
        Long programsInCategory = (Long) session().createCriteria(Program.class)
                .add(Restrictions.eq("category.id", categoryId))
                .setProjection(Projections.rowCount()).uniqueResult();
        Integer maxPage = (int) Math.ceil((double) programsInCategory / itemsPerPage - DECREMENT_BY_ONE);
        return maxPage >= MIN_PAGE_NO ? maxPage : MIN_PAGE_NO;
    }
}
