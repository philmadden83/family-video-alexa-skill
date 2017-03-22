package com.madlab.familyvideo.ext.scraper.parser;

import org.jsoup.nodes.Document;

import java.util.List;

public interface ResultParser<T> {

    List<T> parse(Document response);

}
