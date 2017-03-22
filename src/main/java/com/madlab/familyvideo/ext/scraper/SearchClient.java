package com.madlab.familyvideo.ext.scraper;

import com.madlab.familyvideo.ext.scraper.domain.Category;
import com.madlab.familyvideo.ext.scraper.domain.Game;
import com.madlab.familyvideo.ext.scraper.domain.Movie;
import com.madlab.familyvideo.ext.scraper.domain.Person;
import com.madlab.familyvideo.ext.scraper.exception.ClientException;
import com.madlab.familyvideo.ext.scraper.exception.MultipleResultsFoundExeption;
import com.madlab.familyvideo.ext.scraper.parser.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchClient {
    private static final String URL_FORMAT = "http://www.%s/catalog/advanced_search_result.php?%s";

    private final GameSearchResultParser gameSearchResultParser;
    private final MovieSearchResultParser movieSearchResultParser;
    private final PersonSearchResultParser personSearchResultParser;
    private final String host;

    public SearchClient() {
        this("familyvideo.com", new GameSearchResultParser(), new MovieSearchResultParser(),
                new PersonSearchResultParser());
    }

    public SearchClient(String host, GameSearchResultParser gameSearchResultParser, MovieSearchResultParser movieSearchResultParser,
                        PersonSearchResultParser personSearchResultParser) {
        this.host = host;
        this.gameSearchResultParser = gameSearchResultParser;
        this.personSearchResultParser = personSearchResultParser;
        this.movieSearchResultParser = movieSearchResultParser;
    }

    public List<Movie> searchMovies(String title) throws ClientException {
        return searchMovies(title, Category.DVD);
    }

    public List<Movie> searchMovies(String title, Category format) throws ClientException {
        return search(Jsoup.connect(String.format(URL_FORMAT, host, String.format("title=\"%s\"&categories_id=%d&orderBy=products_date_available&desc=1", title.replaceAll(" ", "+"), format.getId()))), movieSearchResultParser)
                .stream().filter(r -> TextUtil.containsAll(r.getTitle(), title.split(" ")))
                .collect(Collectors.toList());
    }

    public List<Movie> searchMoviesByPerson(String personName, Category format) throws ClientException {
        List<Person> people = searchPeople(personName);

        if (people.isEmpty()) {
            throw new ClientException("No people found");
        }

        if (people.size() > 1) {
            throw new MultipleResultsFoundExeption(people.stream().collect(Collectors.toList()));
        }

        return search(Jsoup.connect(String.format(URL_FORMAT, host, String.format("title=&contributor_id=%d&categories_id=%d&orderBy=products_date_available&desc=1", people.get(0).getId(), format.getId()))), movieSearchResultParser);

    }

    public List<Person> searchPeople(String name) throws ClientException {
        String query = String.format("person=\"%s\"&peopleOnly=1&desc=1&orderBy=FirstName", name.replaceAll(" ", "+"));
        return search(Jsoup.connect(String.format(URL_FORMAT, host, query)), personSearchResultParser)
                .stream().filter(r -> TextUtil.containsAll(r.getDescription(), name.split(" ")))
                .collect(Collectors.toList());
    }

    public List<Game> searchGames(String title) throws ClientException {
        String query = String.format("title=\"%s\"&categories_id=%s&orderBy=products_date_available&desc=1", title.replaceAll(" ", "+"), Category.GAME.getId());
        return search(Jsoup.connect(String.format(URL_FORMAT, host, query)), gameSearchResultParser).stream().filter(r -> TextUtil.containsAll(r.getTitle(), title.split(" ")))
                .collect(Collectors.toList());
    }

    private <T> List<T> search(Connection connection, ResultParser<T> parser) throws ClientException {
        try {
            return parser.parse(connection
                    .timeout(60 * 1000).get());
        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
    }

}
