package com.madlab.familyvideo.ext.scraper.exception;

import com.madlab.familyvideo.ext.scraper.domain.Describable;

import java.util.List;

public class MultipleResultsFoundExeption extends ClientException {
    private final List<Describable> describables;

    public MultipleResultsFoundExeption(List<Describable> describables) {
        super("Too many results found: " + describables.size());
        this.describables = describables;
    }

    public List<Describable> getDescribables() {
        return describables;
    }
}
