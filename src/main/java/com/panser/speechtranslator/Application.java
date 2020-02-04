package com.panser.speechtranslator;

import com.panser.speechtranslator.gcloud.SpeechToText;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import static com.panser.speechtranslator.gcloud.SpeechToText.listAllFilePaths;

public class Application {

    public static final String FLAC_DIRECTORY = "d:\\Downloads\\TipToi\\tttool-1.9\\media\\flac";
    public static final String OUTPUT_FILE_NAME = "transcripts.txt";

    public static void main(String[] args) throws Exception {
        List<String> filePaths = listAllFilePaths(FLAC_DIRECTORY);
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
        filePaths.forEach(filePath -> SpeechToText.translateAndWriteToFile(writer, filePath));
        writer.close();
    }
}
