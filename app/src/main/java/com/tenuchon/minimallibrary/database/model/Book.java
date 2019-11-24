package com.tenuchon.minimallibrary.database.model;

import java.util.Date;
import java.util.UUID;

public class Book {
    private static final String fileNameFormat = "IMG_%s.jpg";
    private UUID id;
    private String title;
    private String author;
    private String description;
    private Date date;
    private boolean favorite;
    private boolean wish;


    public Book() {
        this(
                UUID.randomUUID());
    }

    public Book(UUID id) {
        this.id = id;
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isWish() {
        return wish;
    }

    public void setWish(boolean wish) {
        this.wish = wish;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPhotoFilename() {
        return String.format(fileNameFormat,getId().toString());
    }

}
