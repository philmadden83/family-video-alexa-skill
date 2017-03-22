package com.madlab.familyvideo.ext.scraper;

import com.madlab.familyvideo.ext.scraper.domain.Rental;
import com.madlab.familyvideo.ext.scraper.domain.RentalDateRange;
import com.madlab.familyvideo.ext.scraper.exception.ClientException;
import com.madlab.familyvideo.ext.scraper.parser.RentalsResultParser;
import com.madlab.familyvideo.ext.scraper.parser.TextUtil;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RentalsClient {
    private static final String URL_FORMAT = "http://www.%s/new_releases/%s/%s.php?print=yes";

    private final RentalsResultParser rentalsResultParser;
    private final String host;

    public RentalsClient() {
        this("familyvideo.com", new RentalsResultParser());
    }

    public RentalsClient(String host, RentalsResultParser rentalsResultParser) {
        this.host = host;
        this.rentalsResultParser = rentalsResultParser;
    }

    public List<Rental> getRentals(Date requestDate, RentalDateRange dateRange) throws ClientException {
        try {

            if (requestDate.after(getEndOfNextMonth().getTime())) {
                throw new ClientException("Can only look one month ahead.");
            }

            Calendar tuesday = getClosestHistoricalTuesday(requestDate);

            List<Rental> filteredList = rentalsResultParser.parse(Jsoup.connect(
                    String.format(URL_FORMAT,
                            host,
                            new SimpleDateFormat("yyyy").format(tuesday.getTime()),
                            new SimpleDateFormat("MMMM").format(tuesday.getTime()).toLowerCase()))
                    .get())
                    .stream()
                    .map(m -> correctRentalYear(m, tuesday.get(Calendar.YEAR)))
                    .collect(Collectors.toList());

            switch (dateRange) {
                case DAY:
                case WEEK:
                    return filteredList
                            .stream()
                            .filter(m -> m.getReleaseDate().compareTo(tuesday.getTime()) == 0)
                            .collect(Collectors.toList());
                default:
                    return filteredList;
            }

        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
    }

    public List<Rental> getRental(String title) throws ClientException {
        List<Rental> filteredList;
        try {
            Calendar endOfNextMonth = getEndOfNextMonth();
            filteredList = rentalsResultParser.parse(Jsoup.connect(
                    String.format(URL_FORMAT,
                            host,
                            new SimpleDateFormat("yyyy").format(endOfNextMonth.getTime()),
                            new SimpleDateFormat("MMMM").format(endOfNextMonth.getTime()).toLowerCase()))
                    .get())
                    .stream()
                    .map(r -> correctRentalYear(r, endOfNextMonth.get(Calendar.YEAR)))
                    .filter(r -> TextUtil.containsAll(r.getTitle(), title.split(" ")))
                    .collect(Collectors.toList());

            if (filteredList != null && !filteredList.isEmpty()) {
                return filteredList;
            }
        } catch (IOException e) {
        }
        try {
            //Loop back from the end of this month to for 12 months
            int monthIndex = 0;
            Calendar currentMonth = Calendar.getInstance();
            while(monthIndex++ < 12) {
                filteredList = rentalsResultParser.parse(Jsoup.connect(
                        String.format(URL_FORMAT,
                                host,
                                new SimpleDateFormat("yyyy").format(currentMonth.getTime()),
                                new SimpleDateFormat("MMMM").format(currentMonth.getTime()).toLowerCase()))
                        .get())
                        .stream()
                        .map(r -> correctRentalYear(r, currentMonth.get(Calendar.YEAR)))
                        .filter(r -> TextUtil.containsAll(r.getTitle(), title.split(" ")))
                        .collect(Collectors.toList());

                if (filteredList != null && !filteredList.isEmpty()) {
                    return filteredList;
                }
                currentMonth.add(Calendar.MONTH, -1);
            }
        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        }
        return null;
    }

    private static Rental correctRentalYear(Rental rental, int year) {
        Calendar releaseDateCalendar = Calendar.getInstance();
        releaseDateCalendar.setTime(rental.getReleaseDate());
        releaseDateCalendar.set(Calendar.YEAR, year);

        rental.setReleaseDate(releaseDateCalendar.getTime());
        return rental;
    }

    private static Calendar getEndOfNextMonth() {
        Calendar endNextMonth = Calendar.getInstance();
        endNextMonth.set(Calendar.DATE, 1);
        endNextMonth.add(Calendar.MONTH, 2);
        endNextMonth.add(Calendar.DATE, -1);
        return endNextMonth;
    }

    private static Calendar getClosestHistoricalTuesday(Date requestDate) {
        Calendar requestCalendar = Calendar.getInstance();
        requestCalendar.setTime(requestDate);
        requestCalendar.set(Calendar.HOUR_OF_DAY, 0);
        requestCalendar.set(Calendar.MINUTE, 0);
        requestCalendar.set(Calendar.SECOND, 0);
        requestCalendar.set(Calendar.MILLISECOND, 0);

        if (requestCalendar.get(Calendar.DAY_OF_WEEK) < Calendar.TUESDAY) {
            requestCalendar.add(Calendar.DATE, -7 + (Calendar.TUESDAY - requestCalendar.get(Calendar.DAY_OF_WEEK)));
        } else if (requestCalendar.get(Calendar.DAY_OF_WEEK) > Calendar.TUESDAY) {
            requestCalendar.add(Calendar.DATE, -(requestCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.TUESDAY));
        }
        return requestCalendar;
    }

}
