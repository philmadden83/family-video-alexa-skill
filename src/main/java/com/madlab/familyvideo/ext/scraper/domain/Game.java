package com.madlab.familyvideo.ext.scraper.domain;

public class Game implements Describable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return null;
    }

}
