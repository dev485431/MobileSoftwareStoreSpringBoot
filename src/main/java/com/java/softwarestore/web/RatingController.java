package com.java.softwarestore.web;

import com.java.softwarestore.model.domain.Rating;
import com.java.softwarestore.service.HibernateRatingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class RatingController {

    private static final Logger LOG = LoggerFactory.getLogger(RatingController.class);
    @Autowired
    private HibernateRatingManager ratingManager;

    @RequestMapping(value = "/rating/add", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    private void addRating(@RequestParam(value = "programId") Integer programId, @RequestParam(value = "rating")
            Float rating) {
        Rating newRating = new Rating(rating);
        LOG.debug("Adding new rating: " + rating + " to program id=" + programId);
        ratingManager.addRating(programId, newRating);
    }

}
