package com.java.softwarestore.service.hibernate;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.dto.ProgramDetailsDto;
import com.java.softwarestore.service.PaginationManager;
import com.java.softwarestore.utils.ImageUrlType;
import com.java.softwarestore.utils.RatingsHandler;
import com.java.softwarestore.utils.UrlsHandler;
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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HibernatePaginationManager implements PaginationManager {

    private static final int DECREMENT_BY_ONE = 1;
    private static final int MIN_PAGE_NO = 0;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private UrlsHandler urlsHandler;
    @Autowired
    private RatingsHandler ratingsHandler;
    @Value("${pagination.item.date.format}")
    private String paginationItemDateFormat;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<ProgramDetailsDto> getPage(Integer pageNum, Integer categoryId, Integer itemsPerPage) {
        Integer firstResult = pageNum * itemsPerPage;
        Criteria criteria = session().createCriteria(Program.class);
        List<Program> programs = criteria.add(Restrictions.eq("category.id", categoryId))
                .setCacheable(true)
                .setFirstResult(firstResult)
                .setMaxResults(itemsPerPage).list();

        programs.stream().forEach(program -> {
            Hibernate.initialize(program.getCategory());
            Hibernate.initialize(program.getStatistics());
        });

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(paginationItemDateFormat);
        return programs.stream().map(program -> new ProgramDetailsDto(program.getId(), program.getName(),
                program.getDescription(),
                urlsHandler.getImageUrl(program, ImageUrlType.IMAGE_128),
                urlsHandler.getImageUrl(program, ImageUrlType.IMAGE_512),
                program.getCategory().getName(),
                program.getStatistics().getTimeUploaded().format(dateFormat),
                program.getStatistics().getDownloads(),
                ratingsHandler.getAverageRating(program.getStatistics().getRatings())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMaxPageForCategory(Integer categoryId, Integer itemsPerPage) {
        Long programsInCategory = (Long) session().createCriteria(Program.class)
                .add(Restrictions.eq("category.id", categoryId))
                .setProjection(Projections.rowCount()).uniqueResult();
        Integer maxPage = (int) Math.ceil((double) programsInCategory / itemsPerPage - DECREMENT_BY_ONE);
        return maxPage >= MIN_PAGE_NO ? maxPage : MIN_PAGE_NO;
    }

}
