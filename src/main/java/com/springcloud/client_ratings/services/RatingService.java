package com.springcloud.client_ratings.services;

import com.springcloud.client_ratings.entities.Rating;
import com.springcloud.client_ratings.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    RatingRepository ratingRepository;

    public Rating saveRating(Rating rating) {return ratingRepository.save(rating);}

    public Rating findRatingById(long ratingId) {return ratingRepository.findById(ratingId);}

    public List<Rating> findAllRatings() {return ratingRepository.findAll();}

    public List<Rating> findRatingsByBookId(long bookId) {return ratingRepository.findRatingByBookId(bookId);}

    public Rating createRating(Rating rating) {return ratingRepository.save(rating);}

    public void deleteRating(long ratingId) {ratingRepository.deleteById(ratingId);}

    public Rating updateRating(Rating rating, Long ratingId) {
        rating.setId(ratingId);
        return ratingRepository.save(rating);
    }

}