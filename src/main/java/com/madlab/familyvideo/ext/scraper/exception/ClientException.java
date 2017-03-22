package com.madlab.familyvideo.ext.scraper.exception;

public class ClientException extends Exception {

    public ClientException(String message) {
        this(message, null);
    }

    public ClientException(String message, Throwable t) {
        super(message, t);
    }
}
