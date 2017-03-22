package com.madlab.familyvideo;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazonaws.util.CollectionUtils;
import com.madlab.familyvideo.ext.alexamino.annotation.IntentHandler;
import com.madlab.familyvideo.ext.alexamino.annotation.Slot;
import com.madlab.familyvideo.ext.alexamino.annotation.Utterance;
import com.madlab.familyvideo.ext.scraper.RentalsClient;
import com.madlab.familyvideo.ext.scraper.domain.Rental;
import com.madlab.familyvideo.ext.scraper.domain.RentalDateRange;
import com.madlab.familyvideo.ext.scraper.exception.ClientException;

import java.util.Date;
import java.util.List;

@IntentHandler("ListRentals")
public class ListRentalsIntentHandler {
    private final RentalsClient rentalsClient;

    public ListRentalsIntentHandler() {
        this.rentalsClient = new RentalsClient();
    }

    @Utterance
    public SpeechletResponse getRentalsForDay(@Slot("Day") Date requestedDate) {
        return toText(getRentals(requestedDate, RentalDateRange.DAY));
    }

    @Utterance
    public SpeechletResponse getRentalsForDate(@Slot("Date") Date requestedDate) {
        return toText(getRentals(requestedDate, RentalDateRange.DAY));
    }

    @Utterance
    public SpeechletResponse getRentalsForWeek(@Slot(value = "Date", dateFilter = Slot.DateFilter.WEEK_OF_YEAR) Date requestedDate) {
        return toText(getRentals(requestedDate, RentalDateRange.WEEK));
    }

    @Utterance
    public SpeechletResponse getRentalsForMonth(@Slot("Month") Date requestedDate) {
        return toText(getRentals(requestedDate, RentalDateRange.MONTH));
    }

    private List<Rental> getRentals(Date requestedDate, RentalDateRange dateRange) {
        try {
            return rentalsClient.getRentals(requestedDate, RentalDateRange.DAY);
        } catch (ClientException e) {

        }
        return null;
    }

    private static SpeechletResponse toText(List<Rental> rentals) {
        if (!CollectionUtils.isNullOrEmpty(rentals)) {
            return getRentalsFoundResponse(rentals);
        }
        return getNoRentalsFoundResponse();
    }

    private static SpeechletResponse getRentalsFoundResponse(List<Rental> rentals) {
        StringBuilder builder = new StringBuilder("<speak>");
        builder.append(String.format("<p>I have found %d movies available.</p>", rentals.size()));
        rentals.stream().forEach(r -> builder.append(String.format("<p>%s</p>", r.getTitle())));
        builder.append("</speak>");


        SimpleCard card = new SimpleCard();
        card.setTitle("Family Video : List Rentals");
        card.setContent(String.format("I have found %d movies available.", rentals.size()));

        // Create the plain text output.
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml(builder.toString());

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private static SpeechletResponse getNoRentalsFoundResponse() {
        String speechText = "Sorry. I was unable to find any rentals for the given intent.";
        SimpleCard card = new SimpleCard();
        card.setTitle("Family Video : List Rentals");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

}
