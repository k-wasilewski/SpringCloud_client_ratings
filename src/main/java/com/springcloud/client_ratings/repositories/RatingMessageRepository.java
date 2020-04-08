package com.springcloud.client_ratings.repositories;

import com.springcloud.client_ratings.entities.Rating;
import com.springcloud.client_ratings.entities.RatingMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingMessageRepository extends JpaRepository<RatingMessage, Long> {
    RatingMessage getById(long id);
}