package com.madlab.familyvideo.ext.alexamino.speechlet;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.madlab.familyvideo.ext.alexamino.IntentHandlerContext;

/**
 * Created by Phil on 1/23/17.
 */
//TODO: Wire in classpath scanning via spring. Aliases for annotations.
public class DefaultSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final IntentHandlerContext SPEECHLET_CONTEXT = new IntentHandlerContext();

    static {
        SPEECHLET_CONTEXT.init();
    }

    public DefaultSpeechletRequestStreamHandler() {
        super(new SpeechletDispatcher(SPEECHLET_CONTEXT), SPEECHLET_CONTEXT.getSupportedApplicationIds());
    }

}
