package com.panser.speechtranslator;

import com.panser.speechtranslator.gcloud.SpeechToText;
import com.panser.speechtranslator.gcloud.TextToSpeech;
import com.panser.speechtranslator.gcloud.Translator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.panser.speechtranslator.gcloud.SpeechToText.listAllFilePaths;

public class Application {

    public static final String FLAC_DIRECTORY = "d:\\Downloads\\TipToi\\tttool-1.9\\flac";
    public static final String TRANSCRIPTION_OUTPUT_FILE_NAME = "transcripts.txt";
    public static final String TRANSLATION_OUTPUT_FILE_NAME = "translations.txt";

    public static void main(String[] args) throws IOException {
        speechToTranslatedSpeech();
//        transcriptionsToSpeech("d:\\Downloads\\TipToi\\tttool-1.9\\Jahreszeiten_transcripts.txt");
//        transcriptionsToTranslations("d:\\Downloads\\TipToi\\tttool-1.9\\Jahreszeiten_transcripts.txt");
//        translationsToSpeech("c:\\Users\\panserbj0rn\\repositories\\gcloud-speech-to-text-batch\\temp_translations.txt");
    }

    private static void speechToTranslatedSpeech() throws IOException {
        BufferedWriter transcriptionWriter = new BufferedWriter(new FileWriter(TRANSCRIPTION_OUTPUT_FILE_NAME));
        BufferedWriter translationWriter = new BufferedWriter(new FileWriter(TRANSLATION_OUTPUT_FILE_NAME));

        List<String> filePaths = listAllFilePaths(FLAC_DIRECTORY);

        Map<String, String> filenameToTranscription = filePaths.stream().map(SpeechToText::transcribeFile)
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

    private static void transcriptionsToSpeech(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Pattern DELIMITER = Pattern.compile("=");
        Map<String, String> filenameToTranscription = lines.stream().map(DELIMITER::split)
                .flatMap(a -> IntStream.range(0, a.length - 1).filter(i -> i % 2 == 0)
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(a[i], a[i + 1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
        System.out.println("Transcriptions read: " + filenameToTranscription.size());

        Map<String, String> filenameToTranslation = filenameToTranscription.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Translator.translate(entry.getValue())));
        System.out.println("Translations created: " + filenameToTranslation.size());

        filenameToTranslation.entrySet().forEach(TextToSpeech::textToSpeech);
    }

    private static void translationsToSpeech(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Pattern DELIMITER = Pattern.compile("=");
        Map<String, String> filenameToTranslation = lines.stream().map(DELIMITER::split)
                .flatMap(a -> IntStream.range(0, a.length - 1).filter(i -> i % 2 == 0)
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(a[i], a[i + 1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
        System.out.println("Translations read: " + filenameToTranslation.size());

        filenameToTranslation.entrySet().forEach(TextToSpeech::textToSpeech);
    }

    private static void transcriptionsToTranslations(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Pattern DELIMITER = Pattern.compile("=");
        Map<String, String> filenameToTranscription = lines.stream().map(DELIMITER::split)
                .flatMap(a -> IntStream.range(0, a.length - 1).filter(i -> i % 2 == 0)
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(a[i], a[i + 1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
        System.out.println("Transcriptions read: " + filenameToTranscription.size());

        Map<String, String> filenameToTranslation = filenameToTranscription.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Translator.translate(entry.getValue())));
        System.out.println("Translations created: " + filenameToTranslation.size());

        BufferedWriter translationWriter = new BufferedWriter(new FileWriter("temp_translations.txt"));
//        entries.forEach(entry -> Application.writeEntryToFile(translationWriter, entry));
        int i = 1;
        int totalTranslationCount = filenameToTranslation.entrySet().size();
        for (Map.Entry<String,String> entry : filenameToTranslation.entrySet()) {
            Application.writeEntryToFile(translationWriter, entry);
            System.out.println("Translated " + i + "/" + totalTranslationCount);
        }
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
