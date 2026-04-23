package itmo.lab.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Источник ввода из файла.
 */
public class FileInputSource implements InputSource {

    private BufferedReader reader;

    public FileInputSource(String filePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
    }

    @Override
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}