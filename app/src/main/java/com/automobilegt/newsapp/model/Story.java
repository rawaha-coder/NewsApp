package com.automobilegt.newsapp.model;

public class Story {
    private String mTitle;
    private String mSection;
    private String mWebUrl;
    private String mPublicationDate;
    private String mAuthor;

    public Story(String title, String section, String webUrl, String publicationDate, String author) {
        this.mTitle = title;
        this.mSection = section;
        this.mWebUrl = webUrl;
        this.mPublicationDate = publicationDate;
        this.mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getSection() {
        return mSection;
    }
    public String getWebUrl() {
        return mWebUrl;
    }
    public String getPublicationDate() {
        return mPublicationDate;
    }
    public String getAuthor() {
        return mAuthor;
    }
}