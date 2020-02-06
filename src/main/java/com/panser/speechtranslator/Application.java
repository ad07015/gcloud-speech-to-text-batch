package com.panser.speechtranslator;

import com.panser.speechtranslator.util.FileReaderUtil;

import java.io.IOException;
import java.util.Map;

import static com.panser.speechtranslator.gcloud.SpeechToText.listAllFilePaths;
import static com.panser.speechtranslator.gcloud.SpeechToText.transcribe;
import static com.panser.speechtranslator.gcloud.TextToSpeech.textToSpeech;
import static com.panser.speechtranslator.gcloud.Translator.translate;
import static com.panser.speechtranslator.util.FileWriterUtil.writeToFile;
import static com.panser.speechtranslator.util.Util.filterNonEmptyMapEntries;

public class Application {

    public static final String SPEECH_SOURCE_DIRECTORY = "d:\\Downloads\\TipToi\\tttool-1.9\\flac";
    public static final String TRANSCRIPTION_OUTPUT_FILE_NAME = "transcripts.txt";
    public static final String TRANSLATION_OUTPUT_FILE_NAME = "translations.txt";

    public static void main(String[] args) throws IOException {
        speechToTranslatedSpeech();
    }

    private static void speechToTranslatedSpeech() throws IOException {
        Map<String, String> filenameToTranscription = transcribe(listAllFilePaths(SPEECH_SOURCE_DIRECTORY));
        writeToFile(filenameToTranscription, TRANSCRIPTION_OUTPUT_FILE_NAME);
        Map<String, String> filenameToTranslation = translate(filterNonEmptyMapEntries(filenameToTranscription));
        writeToFile(filenameToTranslation, TRANSLATION_OUTPUT_FILE_NAME);
        textToSpeech(filenameToTranslation);
    }

    private static void transcriptionsToSpeech(String filePath) throws IOException {
        Map<String, String> filenameToTranscription = FileReaderUtil.keyValueFileToMap(filePath);
        Map<String, String> filenameToTranslation = translate(filenameToTranscription);
        textToSpeech(filenameToTranslation);
    }

    private static void translationsToSpeech(String filePath) throws IOException {
        Map<String, String> filenameToTranslation = FileReaderUtil.keyValueFileToMap(filePath);
        textToSpeech(filenameToTranslation);
    }

    private static void transcriptionsToTranslations(String filePath) throws IOException {
        Map<String, String> filenameToTranscription = FileReaderUtil.keyValueFileToMap(filePath);
        Map<String, String> filenameToTranslation = translate(filenameToTranscription);
        writeToFile(filenameToTranslation, "temp_translations.txt");
    }

}
