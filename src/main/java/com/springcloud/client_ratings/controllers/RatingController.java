package com.springcloud.client_ratings.controllers;

import com.springcloud.client_ratings.entities.Rating;
import com.springcloud.client_ratings.kafka.KafkaProducerConfig;
import com.springcloud.client_ratings.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;
    private List<String> messageList= new ArrayList();

    @KafkaListener(topics = "post", groupId = "ratings")
    public void listenToMsgsPost(String message) {
        messageList.add("POST, "+message);
    }

    @KafkaListener(topics = "put", groupId = "ratings")
    public void listenToMsgsPut(String message) {
        messageList.add("PUT, "+message);
    }

    @KafkaListener(topics = "delete", groupId = "ratings")
    public void listenToMsgsDel(String message) {
        messageList.add("DEL, "+message);
    }

    @KafkaListener(topics = "patch", groupId = "ratings")
    public void listenToMsgsPatch(String message) {
        messageList.add("PATCH, "+message);
    }

    @GetMapping("/messages")
    public List<String> getMessageList() { return this.messageList;}

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
        kafkaProducerConfig.sendMessage(rating.niceToString()+" created", "post");
        return ratingService.createRating(rating);
    }

    @DeleteMapping("/{ratingId}")
    public void deleteRating(@PathVariable Long ratingId) {
        Rating rating = ratingService.findRatingById(ratingId);
        kafkaProducerConfig.sendMessage(rating.niceToString()+" deleted", "delete");
        ratingService.deleteRating(ratingId);
    }

    @PutMapping("/{ratingId}")
    public Rating updateRating(@RequestBody Rating rating, @PathVariable Long ratingId) {
        Rating oldRating = ratingService.findRatingById(ratingId);
        Rating newRating = ratingService.updateRating(rating, ratingId);
        kafkaProducerConfig.sendMessage(oldRating.niceToString()+" updated to "+newRating.niceToString(), "put");
        return newRating;
    }

    @PatchMapping("/{ratingId}")
    public Rating updateRating(@RequestBody Map<String, String> updates, @PathVariable Long ratingId) {
        Rating rating = ratingService.findRatingById(ratingId);
        kafkaProducerConfig.sendMessage(rating.niceToString()+" updated with "+updates, "patch");
        updates.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Rating.class, k);
            ReflectionUtils.setField(field, rating, v);
        });
        return ratingService.saveRating(rating);
   }
}