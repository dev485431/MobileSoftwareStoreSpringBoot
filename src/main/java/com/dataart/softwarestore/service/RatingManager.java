package com.dataart.softwarestore.service;

import com.dataart.softwarestore.model.domain.Rating;

public interface RatingManager {

    void addRating(Integer programId, Rating rating);

}
