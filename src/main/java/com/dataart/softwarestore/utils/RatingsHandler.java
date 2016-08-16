package com.dataart.softwarestore.utils;

import com.dataart.softwarestore.model.domain.Rating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class RatingsHandler {

    private static final Double ZERO = 0.0;
    @Value("${program.rating.precision}")
    private int programRatingPrecision;

    public float getAverageRating(List<Rating> ratings) {
        Float averageRating = (float) ratings.stream()
                .map(Rating::getRating)
                .filter(o -> o.isPresent())
                .mapToDouble(Optional::get)
                .average().orElse(ZERO);
        return setPrecision(averageRating, programRatingPrecision);
    }

    private Float setPrecision(float floatValue, int fractionDigits) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
        formatter.setMaximumFractionDigits(fractionDigits);
        formatter.setMinimumFractionDigits(fractionDigits);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return new Float(formatter.format(floatValue));
    }

}
