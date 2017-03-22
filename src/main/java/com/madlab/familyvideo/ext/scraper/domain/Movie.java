package com.madlab.familyvideo.ext.scraper.domain;

import java.util.Date;

public class Movie implements Describable {
    private int id;
    private String title;
    private Date releaseDate;
    private boolean inStock;
    private Double price;
    private String format;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String getDescription() {
        return String.format("%1$s (%2$tB %2$td, %2$tY) - %3$s", title, releaseDate, format);
    }
}
