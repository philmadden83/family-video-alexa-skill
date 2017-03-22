package com.madlab.familyvideo.ext.scraper.domain;

public enum Category {
    DVD (1),
    GAME (4),
    VHS (2),
    BLU_RAY (56);

    private final int id;

    Category(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
