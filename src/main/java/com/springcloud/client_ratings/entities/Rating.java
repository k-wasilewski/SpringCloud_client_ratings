package com.springcloud.client_ratings.entities;

import javax.persistence.*;

@Entity
@Table(name="ratings", schema = "cloud")
public class Rating {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private long bookId;
    private int stars;

    public Rating() {}

    public Rating(long bookId, int stars) {
        this.bookId=bookId;
        this.stars=stars;
    }

    public long getId() {return this.id;}
    public void setId(long id) {this.id=id;}
    public long getBookId() {return this.bookId;}
    public void setbookId(long bookId) {this.bookId=bookId;}
    public int getStars() {return this.stars;}
    public void setStars(int stars) {this.stars=stars;}

    public String niceToString() {return "book with id "+this.bookId+" has "+this.stars+" stars";}
}