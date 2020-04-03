package com.springcloud.client_ratings.controllers;

import com.springcloud.client_ratings.entities.Rating;
import com.springcloud.client_ratings.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
   private RatingService ratingService;

    @GetMapping
    public List<Rating> findRatingsByBookId(@RequestParam(required = false, defaultValue = "0") Long bookId) {

        if (bookId.equals(0L)) {
            return ratingService.findAllRatings();
          }
       return ratingService.findRatingsByBookId(bookId);
    }

    @GetMapping("/{ratingId}")
    public Rating findBook(@PathVariable Long ratingId) {
        return ratingService.findRatingById(ratingId);
    }

    @PostMapping
    public Rating createRating(@RequestBody Rating rating) {
        return ratingService.createRating(rating);
    }

    @DeleteMapping("/{ratingId}")
    public void deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
    }

    @PutMapping("/{ratingId}")
    public Rating updateRating(@RequestBody Rating rating, @PathVariable Long ratingId) {
        return ratingService.updateRating(rating, ratingId);
    }

    @PatchMapping("/{ratingId}")
    public Rating updateRating(@RequestBody Map<String, String> updates, @PathVariable Long ratingId) {
        Rating rating = ratingService.findRatingById(ratingId);

        updates.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Rating.class, k);
            ReflectionUtils.setField(field, rating, v);
        });
        return ratingService.saveRating(rating);
   }
}