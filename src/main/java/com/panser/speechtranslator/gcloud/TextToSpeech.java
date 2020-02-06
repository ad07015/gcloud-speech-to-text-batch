package com.panser.speechtranslator.gcloud;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TextToSpeech {

    public static final String OUTPUT_FORMAT_EXTENSION = "ogg";
    public static final AudioEncoding OUTPUT_ENCODING = AudioEncoding.OGG_OPUS;

    public static void textToSpeech(Map.Entry<String, String> entry) {
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            TimeUnit.MICROSECONDS.sleep(60);
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(entry.getValue())
                    .build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ru-RU")
                    .setSsmlGender(SsmlVoiceGender.FEMALE)
                    .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(OUTPUT_ENCODING)
                    .build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice,
                    audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            // Write the response to the output file.
            String outputFilename = entry.getKey() + "." + OUTPUT_FORMAT_EXTENSION;
            try (OutputStream out = new FileOutputStream(outputFilename)) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"" + outputFilename + "\"");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void textToSpeech(Map<String, String> filenameToTranslation) {
        filenameToTranslation.entrySet().forEach(TextToSpeech::textToSpeech);
    }
}
