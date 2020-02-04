import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GCloudRecognize {

    public static final String FLAC_DIRECTORY = "d:\\Downloads\\TipToi\\tttool-1.9\\media\\flac";
    public static final String OUTPUT_FILE_NAME = "transcripts.txt";

    public static final String SPEECH_LANGUAGE_CODE = "de-DE";
    public static final int SPEECH_SAMPLE_RATE = 22050;
    public static final RecognitionConfig.AudioEncoding SPEECH_ENCODING = RecognitionConfig.AudioEncoding.FLAC;

    public static void main(String[] args) throws Exception {
        List<String> filePaths = listAllFilePaths(FLAC_DIRECTORY);
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
        filePaths.forEach(filePath -> GCloudRecognize.translateAndWriteToFile(writer, filePath));
        writer.close();
    }

    public static void translateAndWriteToFile(BufferedWriter writer, String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            String fileNameNoExtension = file.getName().split("\\.")[0];
            String transcript = syncRecognizeFile(filePath);
            writer.write(fileNameNoExtension + ": " + transcript);
            writer.newLine();
        } catch (Exception e) {
            System.out.println("Failed to transcribe " + file.getName());
        }
    }

    /**
     * Performs speech recognition on raw PCM audio and prints the transcription.
     *
     * @param filePath the path to a PCM audio file to transcribe.
     */
    public static String syncRecognizeFile(String filePath) throws Exception {
        try (SpeechClient speech = SpeechClient.create()) {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Configure request with local raw PCM audio
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(SPEECH_ENCODING)
                            .setLanguageCode(SPEECH_LANGUAGE_CODE)
                            .setSampleRateHertz(SPEECH_SAMPLE_RATE)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Use blocking call to get audio transcript
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                return alternative.getTranscript();
            }
        }
        return "";
    }

    public static List<String> listAllFilePaths(String directory) {
        List<String> result = new LinkedList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(directory))) {

            result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".flac")).collect(Collectors.toList());

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
