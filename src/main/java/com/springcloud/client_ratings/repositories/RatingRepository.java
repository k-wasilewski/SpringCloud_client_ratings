package com.springcloud.client_ratings.repositories;

import com.springcloud.client_ratings.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAll();

    List<Rating> findRatingByBookId(long bookId);

    void deleteById(long ratingId);

    Rating findById(long ratingId);
}