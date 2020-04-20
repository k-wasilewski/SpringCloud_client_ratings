package com.springcloud.client_ratings.controllers;

import com.springcloud.client_ratings.entities.Rating;
import com.springcloud.client_ratings.entities.RatingMessage;
import com.springcloud.client_ratings.kafka.KafkaProducerConfig;
import com.springcloud.client_ratings.repositories.RatingMessageRepository;
import com.springcloud.client_ratings.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private RatingMessageRepository ratingMessageRepository;
    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;
    private Map<LocalDateTime, String> messageList= new HashMap<>();

    @KafkaListener(topics = "post-ratings", groupId = "ratings")
    public void listenToMsgsPost(String message) {
        messageList.put(LocalDateTime.now(), "POST: "+message);
    }

    @KafkaListener(topics = "put-ratings", groupId = "ratings")
    public void listenToMsgsPut(String message) {
        messageList.put(LocalDateTime.now(), "PUT: "+message);
    }

    @KafkaListener(topics = "delete-ratings", groupId = "ratings")
    public void listenToMsgsDel(String message) {
        messageList.put(LocalDateTime.now(), "DEL: "+message);
    }

    @KafkaListener(topics = "patch-ratings", groupId = "ratings")
    public void listenToMsgsPatch(String message) {
        messageList.put(LocalDateTime.now(), "PATCH: "+message);
    }

    @GetMapping(value = "/messages", produces = {"application/json"})
    public Map<LocalDateTime, String> getMessageList() { return this.messageList;}

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
        String message = rating.niceToString()+" created";
        RatingMessage dbMessage = new RatingMessage(LocalDateTime.now(), "POST", message);
        ratingMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(message, "post-ratings");
        return ratingService.createRating(rating);
    }

    @DeleteMapping("/{ratingId}")
    public void deleteRating(@PathVariable Long ratingId) {
        Rating rating = ratingService.findRatingById(ratingId);
        String message = rating.niceToString()+" deleted";
        RatingMessage dbMessage = new RatingMessage(LocalDateTime.now(), "POST", message);
        ratingMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(message, "delete-ratings");
        ratingService.deleteRating(ratingId);
    }

    @PutMapping("/{ratingId}")
    public Rating updateRating(@RequestBody Rating rating, @PathVariable Long ratingId) {
        Rating oldRating = ratingService.findRatingById(ratingId);
        Rating newRating = ratingService.updateRating(rating, ratingId);
        String message = oldRating.niceToString()+" updated to "+newRating.niceToString();
        RatingMessage dbMessage = new RatingMessage(LocalDateTime.now(), "POST", message);
        ratingMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(message, "put-ratings");
        return newRating;
    }

    @PatchMapping("/{ratingId}")
    public Rating updateRating(@RequestBody Map<String, String> updates, @PathVariable Long ratingId) {
        Rating rating = ratingService.findRatingById(ratingId);
        String message = rating.niceToString()+" updated with "+updates;
        RatingMessage dbMessage = new RatingMessage(LocalDateTime.now(), "POST", message);
        ratingMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(message, "patch-ratings");
        updates.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Rating.class, k);
            ReflectionUtils.setField(field, rating, v);
        });
        return ratingService.saveRating(rating);
   }
}