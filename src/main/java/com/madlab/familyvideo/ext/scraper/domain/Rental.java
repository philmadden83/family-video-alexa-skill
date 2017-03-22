package com.madlab.familyvideo.ext.scraper.domain;

import java.util.Date;

public class Rental implements Describable {
    private int id;
    private String title;
    private Date releaseDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String getDescription() {
        return String.format("%1$s (%2$tB %2$td, %2$tY)", title, releaseDate);
    }
}
