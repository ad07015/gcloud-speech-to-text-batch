package com.panser.speechtranslator;

import com.panser.speechtranslator.gcloud.SpeechToText;
import com.panser.speechtranslator.gcloud.TextToSpeech;
import com.panser.speechtranslator.gcloud.Translator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.panser.speechtranslator.gcloud.SpeechToText.listAllFilePaths;

public class Application {

    public static final String FLAC_DIRECTORY = "d:\\Downloads\\TipToi\\tttool-1.9\\media\\flac";
    public static final String TRANSCRIPTION_OUTPUT_FILE_NAME = "transcripts.txt";
    public static final String TRANSLATION_OUTPUT_FILE_NAME = "translations.txt";

    public static void main(String[] args) throws IOException {
        speechToTranslatedSpeech();
    }

    private static void speechToTranslatedSpeech() throws IOException {
        BufferedWriter transcriptionWriter = new BufferedWriter(new FileWriter(TRANSCRIPTION_OUTPUT_FILE_NAME));
        BufferedWriter translationWriter = new BufferedWriter(new FileWriter(TRANSLATION_OUTPUT_FILE_NAME));

        List<String> filePaths = listAllFilePaths(FLAC_DIRECTORY);

        Map<String, String> filenameToTranscription = filePaths.subList(0, 5).stream().map(SpeechToText::transcribeFile)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        filenameToTranscription.entrySet().forEach(entry -> writeEntryToFile(transcriptionWriter, entry));
        transcriptionWriter.close();

        Map<String, String> filenameToNonEmptyTranscription = filenameToTranscription.entrySet().stream().filter(entry -> !entry.getValue().isBlank())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, String> filenameToTranslation = filenameToNonEmptyTranscription.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Translator.translate(entry.getValue())));

        filenameToTranslation.entrySet().forEach(entry -> writeEntryToFile(translationWriter, entry));
        translationWriter.close();

        filenameToTranslation.entrySet().forEach(TextToSpeech::textToSpeech);
    }

    public static void writeEntryToFile(BufferedWriter writer, Map.Entry<String, String> entry) {
        try {
            writer.write(entry.getKey() + "=" + entry.getValue());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
