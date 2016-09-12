package com.java.softwarestore.model.dto;

import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.service.HibernateRatingManager;
import com.java.softwarestore.utils.ImageUrlType;
import com.java.softwarestore.utils.UrlsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class ProgramDetailsDtoFactory {

    private static final Double ZERO = 0.0;
    @Autowired
    private UrlsHandler urlsHandler;
    @Autowired
    private HibernateRatingManager hibernateRatingManager;
    @Value("${program.rating.precision}")
    private int programRatingPrecision;

    public ProgramDetailsDto getDetailsDto(Program program, String dateFormat) {
        return new ProgramDetailsDto(program.getId(), program.getName(),
                program.getDescription(),
                urlsHandler.getURI(program, ImageUrlType.IMAGE_128),
                urlsHandler.getURI(program, ImageUrlType.IMAGE_512),
                program.getCategory().getName(),
                program.getStatistics().getTimeUploaded().format(DateTimeFormatter.ofPattern(dateFormat)),
                program.getStatistics().getDownloads(),
                setPrecision(hibernateRatingManager.getAverageRating(program.getId()).orElse(ZERO).floatValue(),
                        programRatingPrecision));
    }

    private Float setPrecision(float floatValue, int fractionDigits) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
        formatter.setMaximumFractionDigits(fractionDigits);
        formatter.setMinimumFractionDigits(fractionDigits);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return new Float(formatter.format(floatValue));
    }

}
