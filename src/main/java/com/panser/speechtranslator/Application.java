package com.panser.speechtranslator;

import com.panser.speechtranslator.gcloud.Translator;
import com.panser.speechtranslator.gcloud.SpeechToText;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.panser.speechtranslator.gcloud.SpeechToText.listAllFilePaths;

public class Application {

    public static final String FLAC_DIRECTORY = "d:\\Downloads\\TipToi\\tttool-1.9\\media\\flac";
    public static final String OUTPUT_FILE_NAME = "transcripts.txt";

    public static void main(String[] args) throws Exception {
        List<String> filePaths = listAllFilePaths(FLAC_DIRECTORY);
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
        Map<String, String> filenameToTranscription = filePaths.subList(0, 5).stream().map(SpeechToText::transcribeFile)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> filenameToTranslation = filenameToTranscription.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Translator.translate(entry.getValue())));

        filenameToTranslation.entrySet().forEach(entry -> writeEntryToFile(writer, entry));
        writer.close();
    }

    public static void writeEntryToFile(BufferedWriter writer, Map.Entry<String, String> entry) {
        try {
            writer.write(entry.getKey() + ": " + entry.getValue());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
