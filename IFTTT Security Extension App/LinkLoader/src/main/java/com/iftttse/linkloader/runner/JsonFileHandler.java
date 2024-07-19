package com.iftttse.linkloader.runner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.iftttse.linkloader.runner.LinkData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonFileHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    public int getLastLinkDataIndex(String fileName) {
        List<LinkData> linkDataList = readFromJsonFile(fileName);
        if (linkDataList.isEmpty()) {
            return -1; // Restituisce -1 se l'array è vuoto
        }
        return linkDataList.size() - 1; // Restituisce l'indice dell'ultimo elemento
    }

    public void saveStringsToJsonFile(List<String> strings, String fileName) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), strings);
            System.out.println("Dati salvati nel file " + fileName);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei dati nel file JSON: " + e.getMessage());
        }
    }

    public List<String> readStringsFromJsonFile(String fileName) {
        try {
            return objectMapper.readValue(new File(fileName), new TypeReference<List<String>>() {});
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file JSON: " + e.getMessage());
            return List.of();  // Ritorna una lista vuota in caso di errore
        }
    }

    private List<LinkData> readFromJsonFile(String fileName) {
        List<LinkData> linkDataList = List.of();
        File file = new File(fileName);
        if (file.exists()) {
            try {
                linkDataList = objectMapper.readValue(file, new TypeReference<List<LinkData>>() {});
            } catch (IOException e) {
                System.err.println("Errore durante la lettura del file JSON: " + e.getMessage());
            }
        }
        return linkDataList;
    }
    public void appendLinkDataToFile(LinkData linkData, String fileName) {
        File file = new File(fileName);
        boolean fileExists = file.exists();

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            if (fileExists) {
                // Add a comma and newline if the file already has content (i.e., it's not an empty array)
                fileWriter.write(",\n");
            } else {
                // If the file is new, start the array
                fileWriter.write("[\n");
            }
            writer.writeValue(fileWriter, linkData);
        } catch (IOException e) {
            System.err.println("Errore durante l'append dei dati nel file JSON: " + e.getMessage());
        }

        // Close the array properly
        if (!fileExists) {
            try (FileWriter fileWriter = new FileWriter(file, true)) {
                fileWriter.write("\n]");
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura dell'array JSON: " + e.getMessage());
            }
        }
    }
}
