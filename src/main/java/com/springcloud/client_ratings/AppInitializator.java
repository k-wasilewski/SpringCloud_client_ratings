package com.springcloud.client_ratings;

import com.springcloud.client_ratings.controllers.RatingController;
import com.springcloud.client_ratings.entities.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * for testing purposes
 */
@Component
class AppInitializator {
    @Autowired
    RatingController ratingController;

    @PostConstruct
    private void init() {
        ratingController.createRating(new Rating(666, 666));
    }
}
