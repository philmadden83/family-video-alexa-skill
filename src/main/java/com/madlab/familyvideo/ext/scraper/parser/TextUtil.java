package com.madlab.familyvideo.ext.scraper.parser;

import java.util.Set;

public final class TextUtil {

    public static String removeKeyWords(String text, Set<String> words) {
        String result = text;
        for (String key : words) {
            result = result.replaceAll(key, "");
        }
        return result.trim();
    }

    public static boolean containsAll(String s, String... words) {
        for (String word : words) {
            if (!s.toLowerCase().contains(word.toLowerCase())) {
                return false;
            }
        }
        return  true;
    }
}
