package com.java.softwarestore.service;

import com.java.softwarestore.model.domain.Rating;

public interface RatingManager {

    void addRating(Integer programId, Rating rating);

}
