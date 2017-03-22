package com.madlab.familyvideo.ext.scraper.parser;

import com.madlab.familyvideo.ext.scraper.domain.Rental;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RentalsResultParser implements ResultParser<Rental> {
    private static final String RENTAL_MOVIE_TITLE_QUERY    = "td.dvdTitle";
    private static final Pattern MOVIE_ID_PATTERN = Pattern.compile("/catalog/product_info\\.php\\?products_id=(\\d+)");

    @Override
    public List<Rental> parse(Document response) {
        List<Rental> rentals = new ArrayList<>();
        Elements movieTitleElements = response.select(RENTAL_MOVIE_TITLE_QUERY);
        Rental rental;
        int lastReleaseDateElementIndex = 0;
        for (Element movieElement : movieTitleElements) {
            rental = new Rental();
            Element movieLink = movieElement.select("a").first();
            Matcher matcher = MOVIE_ID_PATTERN.matcher(movieLink.attr("href"));
            if (matcher.find()) {
                rental.setId(Integer.parseInt(matcher.group(1)));
            }

            Element movieDataRow = movieElement.parent().parent().parent().parent().parent();
            Element movieReleaseDateElement = movieDataRow.siblingElements().get(movieDataRow.elementSiblingIndex() - 3);

            if (!movieReleaseDateElement.select("td.releaseDate").isEmpty()) {
                lastReleaseDateElementIndex = movieDataRow.elementSiblingIndex() - 3;
            }

            movieReleaseDateElement = movieDataRow.siblingElements().get(lastReleaseDateElementIndex).child(0);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM ddd");
            try {
                rental.setReleaseDate(simpleDateFormat.parse(movieReleaseDateElement.html().replaceAll("<a name=\"[\\w\\d]+\" id=\"[\\w\\d]+\"></a>", "")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            rental.setTitle(movieLink.html());
            rentals.add(rental);
        }
        return rentals;
    }
}
