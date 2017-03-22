package com.madlab.familyvideo.ext.scraper.parser;

import com.madlab.familyvideo.ext.scraper.domain.Person;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonSearchResultParser implements ResultParser<Person> {
    private static final String RESULT_TABLE_QUERY      = "table.rh3resultsRowBottom";
    private static final String PERSON_DATA_COLUMNS_QUERY = "table td";
    private static final Pattern PERSON_ID_PATTERN      = Pattern.compile("advanced_search_result\\.php\\?contributor_id=(\\d+)");

    @Override
    public List<Person> parse(Document response) {
        List<Person> people = new ArrayList<>();
        Elements resultTables = response.select(RESULT_TABLE_QUERY);
        Person person;
        Elements columns;
        int id;

        for (Element personElement : resultTables) {
            person = new Person();
            columns = personElement.select(PERSON_DATA_COLUMNS_QUERY);

            if (columns.size() > 0) {
                Matcher matcher = PERSON_ID_PATTERN.matcher(columns.get(0).child(0).attr("href"));
                if (matcher.find()) {
                    person.setId(Integer.parseInt(matcher.group(1)));
                }

                person.setFirstName(columns.get(0).child(0).html());

                if (columns.size() > 1) {
                    person.setLastName(columns.get(1).child(0).html());
                }

                if (columns.size() > 2) {
                    person.setShortDescription(columns.get(2).html());
                }

            }

            people.add(person);
        }

        return people;
    }

}
