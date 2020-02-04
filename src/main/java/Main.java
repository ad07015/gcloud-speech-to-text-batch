import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "gcloud ml speech recognize c:\\Users\\panserbj0rn\\Downloads\\foo.flac --language-code=de-DE");
        builder.redirectErrorStream(true);
        Process p = builder.start();

        File fout = new File("out.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            bw.write(line);
            bw.newLine();
            System.out.println(line);
        }
        bw.close();
    }
}
