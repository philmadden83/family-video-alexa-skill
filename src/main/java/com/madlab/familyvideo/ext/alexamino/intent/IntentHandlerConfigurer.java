package com.madlab.familyvideo.ext.alexamino.intent;

import com.madlab.familyvideo.ext.alexamino.annotation.Utterance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 1/23/17.
 */
public class IntentHandlerConfigurer {

    /**
     * This method will
     * @param handler
     * @return
     */
    public IntentHandler configure(Class<?> handler) {
        List<UtteranceHandlerContext> utteranceHandlerContexts = Arrays
                .stream(handler.getMethods())
                .filter(m -> m.isAnnotationPresent(Utterance.class))
                .map(hm -> new UtteranceHandlerContext(hm))
                .collect(Collectors.toList());

        if (utteranceHandlerContexts.isEmpty()) {
            //TODO: throw execption
        }

        if (utteranceHandlerContexts.size() > 1) {
            //TODO: throw exception?
        }

        try {
            Object instance = handler.newInstance();
            return new IntentHandler(instance, utteranceHandlerContexts);

        } catch (InstantiationException | IllegalAccessException e) {
            //TODO: Throw exception
            e.printStackTrace();
        }

        return null;
    }

}
