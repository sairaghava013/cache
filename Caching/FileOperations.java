package Caching;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class FileOperations {
    private BufferedReader br;

    public FileOperations(File file) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(file));
    }

    String getNextLine() throws IOException {
        String line = null;
        if (br.ready()) {
            line = br.readLine();
        }
        return line;
    }
}
