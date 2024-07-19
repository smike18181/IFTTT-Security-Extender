package com.iftttse.dbloader.service.applet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AppletLoader {

    public static class Applet {
        public List<String> appletLinks;

        public List<String> getAppletLinks() {
            return appletLinks;
        }

        public void setAppletLinks(List<String> appletLinks) {
            this.appletLinks = appletLinks;
        }
    }

    public List<List<String>> loadAppletsLinks(String resourcePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<List<String>> links = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }
            // Leggi il file JSON e mappalo a una lista di Applet
            List<Applet> applets = objectMapper.readValue(inputStream, new TypeReference<List<Applet>>() {});

            // Estrai solo il primo valore di appletLinks per ciascun oggetto
            for (Applet applet : applets) {
                if (applet.getAppletLinks() != null && !applet.getAppletLinks().isEmpty()) {
                    List<String> firstLink = new ArrayList<>();
                    firstLink.add(applet.getAppletLinks().get(0));
                    links.add(firstLink);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }
}
