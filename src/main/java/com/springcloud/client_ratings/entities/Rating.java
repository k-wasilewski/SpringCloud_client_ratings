package com.springcloud.client_ratings.entities;

import javax.persistence.*;

@Entity
@Table(name="ratings")
public class Rating {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private long bookId;
    private int stars;

    public long getId() {return this.id;}
    public void setId(long id) {this.id=id;}
    public long getBookId() {return this.bookId;}
    public void setbookId(long bookId) {this.bookId=bookId;}
    public int getstars() {return this.stars;}
    public void setstars(int stars) {this.stars=stars;}
}