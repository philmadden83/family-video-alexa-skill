package com.madlab.familyvideo.ext.alexamino;

import com.madlab.familyvideo.ext.alexamino.intent.IntentHandler;
import com.madlab.familyvideo.ext.alexamino.intent.IntentHandlerConfigurer;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Phil on 1/22/17.
 */
public class IntentHandlerContext {
    private static final Map<String, IntentHandler> INTENT_HANDLERS = new HashMap<>();
    private static final IntentHandlerConfigurer INTENT_HANDLER_CONFIGURER = new IntentHandlerConfigurer();

    public static void init() {
        new Reflections("").getTypesAnnotatedWith(com.madlab.familyvideo.ext.alexamino.annotation.IntentHandler.class)
                .stream()
                .forEach(ih ->
                        INTENT_HANDLERS.put(ih.getDeclaredAnnotation(com.madlab.familyvideo.ext.alexamino.annotation.IntentHandler.class).value(), INTENT_HANDLER_CONFIGURER.configure(ih)));
    }

    public Set<String> getSupportedApplicationIds() {
        return new HashSet<String>() {{
            add("amzn1.ask.skill.cd83df1e-4b7e-417b-9071-621a9a1ada44");
        }};
    }

    public IntentHandler getIntentHandler(String intentName) {
        return INTENT_HANDLERS.get(intentName);
    }

}
