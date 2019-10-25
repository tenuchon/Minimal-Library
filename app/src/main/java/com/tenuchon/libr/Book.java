package com.tenuchon.libr;

import java.util.UUID;

public class Book {
    private UUID mId;
    private String mTitle;
    private String mAuthor;
    private String mDescription;
    private boolean mFavorite;

    public Book() {
        this(UUID.randomUUID());
    }

    public Book(UUID id) {
        this.mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

}
