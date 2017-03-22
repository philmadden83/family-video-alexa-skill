package com.madlab.familyvideo.ext.scraper.parser;

import com.madlab.familyvideo.ext.scraper.domain.Game;
import org.jsoup.nodes.Document;

import java.util.List;

public class GameSearchResultParser implements ResultParser<Game> {

    @Override
    public List<Game> parse(Document response) {
        System.out.println(response);
        return null;
    }

}
