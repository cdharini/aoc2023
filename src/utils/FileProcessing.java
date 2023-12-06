package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class FileProcessing {
        public static List<String> readFile(final String path) {
            List<String> linesInFile = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    linesInFile.add(line);
                }
            } catch (IOException e) {
                System.out.println("Exception reading file - " + e);
            }
            return linesInFile;
        }

    }
