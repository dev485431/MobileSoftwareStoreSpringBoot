package com.java.softwarestore.service.hibernate;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.dto.ProgramDetailsDto;
import com.java.softwarestore.service.MostPopularManager;
import com.java.softwarestore.service.QueryResultsOrder;
import com.java.softwarestore.utils.ImageUrlType;
import com.java.softwarestore.utils.RatingsHandler;
import com.java.softwarestore.utils.UrlsHandler;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HibernateMostPopularManager implements MostPopularManager {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private UrlsHandler urlsHandler;
    @Autowired
    private RatingsHandler ratingsHandler;
    @Value("${popular.item.date.format}")
    private String popularItemDateFormat;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<ProgramDetailsDto> getTopPrograms(Integer limit, QueryResultsOrder downloadOrder, QueryResultsOrder
            timeUploadedOrder) {
        String query = "from Program p order by p.statistics.downloads " + downloadOrder.value() + ", p.statistics" +
                ".timeUploaded " + timeUploadedOrder.value();
        List<Program> programs = session().createQuery(query).setMaxResults(limit).setCacheable(true).list();

        programs.stream().forEach(program -> {
            Hibernate.initialize(program.getCategory());
            Hibernate.initialize(program.getStatistics());
        });

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(popularItemDateFormat);
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
}


