package com.springcloud.client_ratings;

import com.springcloud.client_ratings.entities.Rating;
import com.springcloud.client_ratings.repositories.RatingMessageRepository;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

@SpringBootTest
class ClientRatingsApplicationTests {
    @Autowired
    private RatingMessageRepository ratingMessageRepository;
    private final String ROOT_URI = "http://localhost:8084";
    private FormAuthConfig formConfig
            = new FormAuthConfig("/login", "username", "password");

    @Before
    public void setup() {
        RestAssured.config = RestAssured.config().redirect(
                RedirectConfig.redirectConfig().followRedirects(false));
    }

    @Test
    public void whenGetAllRatings_thenSuccess() {
        Response response = RestAssured.get(ROOT_URI + "/rating-service/ratings");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void whenAddNewRating_thenSuccess() {
        Rating rating = new Rating(1L, 4);
        Response ratingResponse = RestAssured.given().auth()
                .form("admin", "admin", formConfig).and()
                .contentType(ContentType.JSON)
                .body(rating)
                .post(ROOT_URI + "/rating-service/ratings");
        Rating result = ratingResponse.as(Rating.class);

        Assert.assertEquals(HttpStatus.OK.value(), ratingResponse.getStatusCode());
        Assert.assertEquals(rating.getBookId(), result.getBookId());
        Assert.assertEquals(rating.getStars(), result.getStars());
    }

    @Test
    public void whenAddNewRating_thenRatingMessage() {
        Rating rating = new Rating(1L, 4);
        Response ratingResponse = RestAssured.given().auth()
                .form("admin", "admin", formConfig).and()
                .contentType(ContentType.JSON)
                .body(rating)
                .post(ROOT_URI + "/rating-service/ratings");
        String message = rating.niceToString()+" created";

        Assert.assertEquals(HttpStatus.OK.value(), ratingResponse.getStatusCode());
        Assert.assertEquals(message, ratingMessageRepository.getById(1).getMessage());
    }

    @Test
    public void whenAccessProtectedResourceWithoutLogin_thenRedirectToLogin() throws Exception {
        Response response = RestAssured.get(ROOT_URI + "/rating-service/ratings/1");

        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .when()
                .redirects().follow(false)
                .get(ROOT_URI + "/rating-service/ratings/1")
                .then()
                .statusCode(302)
                .header("Location", "http://localhost:8084/login");
    }

    @Test
    public void whenAccessProtectedResourceAfterLogin_thenSuccess() {
        Response response = RestAssured.given().auth()
                .form("user", "password", formConfig)
                .get(ROOT_URI + "/rating-service/ratings/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void whenAdminAccessDiscoveryResource_thenSuccess() {
        Response response = RestAssured.given().auth()
                .form("admin", "admin", formConfig)
                .get(ROOT_URI + "/discovery");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

}
