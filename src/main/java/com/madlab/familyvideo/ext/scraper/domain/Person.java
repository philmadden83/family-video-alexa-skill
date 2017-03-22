package com.madlab.familyvideo.ext.scraper.domain;

public class Person implements Describable {
    private int id;
    private String firstName;
    private String lastName;
    private String shortDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @Override
    public String getDescription() {
        return String.format("%s %s. %s", firstName, lastName, shortDescription);
    }
}
