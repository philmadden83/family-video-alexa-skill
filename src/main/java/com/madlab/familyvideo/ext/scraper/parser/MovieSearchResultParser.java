package com.madlab.familyvideo.ext.scraper.parser;

import com.madlab.familyvideo.ext.scraper.domain.Movie;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieSearchResultParser implements ResultParser<Movie> {
    private static final String RESULT_TABLE_QUERY              = "table.rh3resultsRowBottom";
    private static final String MOVIE_DATA_COLUMNS_QUERY        = "table td";
    private static final Pattern MOVIE_ID_PATTERN               = Pattern.compile("/catalog/product_info\\.php\\?products_id=(\\d+)");
    private static final Set<String> REPLACE_WORDS              = new HashSet<String>() {{
        add("Blu-ray");
        add("Blu-ray Disc");
        add("Blu-ray/DVD");
        add("4K Ultra HD");
        add("DVD");
        add("Includes Digital Copy");
        add("UltraViolet Blu-ray Disc");
        add("UltraViolet Blu-ray");
        add("[;\\/]");
    }};


    @Override
    public List<Movie> parse(Document response) {
        List<Movie> movies = new ArrayList<>();
        Elements resultTables = response.select(RESULT_TABLE_QUERY);
        Movie movie;
        Elements columns;
        int id;

        for (Element movieElement : resultTables) {
            movie = new Movie();
            columns = movieElement.select(MOVIE_DATA_COLUMNS_QUERY);

            if (columns.size() > 1) {
                Matcher matcher = MOVIE_ID_PATTERN.matcher(columns.get(0).child(0).attr("href"));
                if (matcher.find()) {
                    movie.setId(Integer.parseInt(matcher.group(1)));
                }
                movie.setTitle(TextUtil.removeKeyWords(columns.get(1).select("a").first().html(), REPLACE_WORDS));

                try {
                    SimpleDateFormat releaseDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    movie.setReleaseDate(releaseDateFormat.parse(columns.get(1).select("span.rh3searchResultsStreet").html().replaceAll("Release Date: ", "")));
                } catch (ParseException e) {

                }

                movie.setInStock(columns.get(1).select("span.browsePrebook").html().contains("In Stock"));
                movie.setFormat(columns.get(2).child(0).html());
            }

            movies.add(movie);
        }

        return movies;
    }
}
