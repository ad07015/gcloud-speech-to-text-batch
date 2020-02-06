package com.panser.speechtranslator.gcloud;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Translator {

    public static String translate(String stringToBeTranslated) {
        return translate(List.of(stringToBeTranslated)).get(0);
    }

    public static List<String> translate(List<String> stringsToBeTranslated) {
        List<String> result = new LinkedList<>();
        try {
            Translate translate = TranslateOptions.getDefaultInstance().getService();
            TimeUnit.MICROSECONDS.sleep(60);
            List<Translation> translations = translate.translate(stringsToBeTranslated, Translate.TranslateOption.targetLanguage("ru"));
            result = translations.stream().map(Translation::getTranslatedText).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
