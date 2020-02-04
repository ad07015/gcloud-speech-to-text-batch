package com.panser.speechtranslator.gcloud;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.List;
import java.util.stream.Collectors;

public class Translator {

    private static String TRANSLATED_TRANSCRIPTS_FILE_PATH = "transcripts_translated.txt";

    public static String translate(String stringToBeTranslated) {
        return translate(List.of(stringToBeTranslated)).get(0);
    }

    public static List<String> translate(List<String> stringToBeTranslated) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        List<Translation> translations = translate.translate(stringToBeTranslated, Translate.TranslateOption.targetLanguage("ru"));
        return translations.stream().map(Translation::getTranslatedText).collect(Collectors.toList());
    }
}
