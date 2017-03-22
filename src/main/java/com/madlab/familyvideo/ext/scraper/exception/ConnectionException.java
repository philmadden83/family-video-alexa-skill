package com.madlab.familyvideo.ext.scraper.exception;

import java.net.URL;

public class ConnectionException extends ClientException {

    public ConnectionException(URL url, Throwable t) {
        super(String.format("Unable to connect to host: %s.", url.getHost()), t);
    }
}
