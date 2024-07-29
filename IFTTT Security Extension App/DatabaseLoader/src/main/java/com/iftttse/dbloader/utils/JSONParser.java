package com.iftttse.dbloader.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JSONParser {

    private static final Logger logger = LoggerFactory.getLogger(JSONParser.class);

    public static void saveElementsToJson(List<Elements> elementsList, String fileName) {
        List<List<String>> elementsTextList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        for (Elements elements : elementsList) {
            List<String> textList = new ArrayList<>();
            for (Element element : elements) {
                textList.add(element.outerHtml());
            }
            elementsTextList.add(textList);
        }

        try {
            objectMapper.writeValue(new File(fileName), elementsTextList);
            logger.info("Salvato gli elementi nel file JSON: {}", fileName);
        } catch (IOException e) {
            logger.error("Errore durante il salvataggio degli elementi nel file JSON", e);
        }
    }

    public static void saveElementToJson(Elements elements, String fileName) {
        List<String> textList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File(fileName);

        // Converti gli elementi in una lista di stringhe
        for (Element element : elements) {
            textList.add(element.outerHtml());
        }

        List<List<String>> existingElementsTextList = new ArrayList<>();
        if (file.exists()) {
            try {
                // Leggi il contenuto esistente
                existingElementsTextList = objectMapper.readValue(file, new TypeReference<List<List<String>>>() {});
            } catch (IOException e) {
                logger.warn("Errore durante la lettura del file JSON esistente: {}", fileName, e);
            }
        }

        // Aggiungi il nuovo elemento
        existingElementsTextList.add(textList);

        // Riscrivi il file con i nuovi dati
        try {
            objectMapper.writeValue(file, existingElementsTextList);
            logger.info("Salvato l'elemento nel file JSON: {}", fileName);
        } catch (IOException e) {
            logger.error("Errore durante il salvataggio dell'elemento nel file JSON", e);
        }
    }

    public static List<Elements> readElementsFromJson(String fileName) {
        List<List<String>> elementsTextList;
        List<Elements> elementsList = new ArrayList<>();

        try (InputStream inputStream = JSONParser.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                logger.warn("Il file JSON non esiste nella cartella resources: {}", fileName);
                return null;
            }

            elementsTextList = new ObjectMapper().readValue(inputStream, new TypeReference<List<List<String>>>() {});

            for (List<String> textList : elementsTextList) {

                Elements elements = new Elements();
                for (String html : textList) {

                    elements.add(Jsoup.parse(html).body().child(0));
                }
                elementsList.add(elements);
            }
            logger.info("Lettura degli elementi dal file JSON completata: {}", fileName);
        } catch (IOException e) {
            logger.error("Errore durante la lettura degli elementi dal file JSON", e);
        }

        return elementsList;
    }

    public static int countElementsInJson(String fileName) {
        int count = 0;

        try (InputStream inputStream = JSONParser.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                logger.warn("Il file JSON '{}' non esiste nella cartella resources.", fileName);
                return count;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);

            if (rootNode.isArray()) {
                for (JsonNode mapNode : rootNode) {
                    if (mapNode.isObject()) {
                        count++; // Incrementa il contatore per ogni oggetto nella lista
                    } else {
                        logger.warn("Elemento non è un oggetto nel file JSON '{}'", fileName);
                    }
                }
                logger.info("Lettura degli elementi dal file JSON completata: '{}'", fileName);
            } else {
                logger.warn("Il file JSON '{}' non contiene un array alla radice.", fileName);
            }
        } catch (IOException e) {
            logger.error("Errore durante la lettura del file JSON '{}'", fileName, e);
        }

        logger.info("Numero totale di elementi nel file JSON '{}': {}", fileName, count);
        return count;
    }


    public static Map<String, Elements>  readElementsFromJson(String fileName, String field) {
        Map<String, Elements> applets = new HashMap<>();

        try (InputStream inputStream = JSONParser.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                logger.info("non esiste il file per intellij");
                logger.warn("Il file JSON '{}' non esiste nella cartella resources.", fileName);
                return applets; // Restituisce una lista vuota invece di null
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);

            if (rootNode.isArray()) {
                int max = countElementsInJson("details.json");
                logger.info("{}",max);
                int counter=-1;

                for (int i = 0; i < rootNode.size(); i++) {

                    JsonNode node = rootNode.get(i);
                    JsonNode appletLinksNode = node.path(field);

                    if (appletLinksNode.isArray()) {
                        for (int j = 0; j < appletLinksNode.size(); j++) {

                            if(counter++<max){
                               // logger.info("COUNTER: {}",counter);
                                continue;
                            }

                            String url = appletLinksNode.get(j).asText();
                            logger.info("URL {}: {}", counter, url);
                            Elements elements = new Elements();

                            try {
                                Document document = Jsoup.connect(url).get();
                                Element body = document.body();

                                if (body.childrenSize() > 0) {
                                    Element name = body.selectFirst(".connection-title");
                                    Elements element = body.select(".applet-details");

                                    if(name != null) {
                                        applets.put(name.text(), element);
                                        logger.debug("Aggiunti {} elementi alla lista degli applet", element.size());

                                        JSONParser.saveElementAndNameToJson(element, name.text(), "details.json");
                                    }
                                } else {
                                    logger.warn("Nessun elemento figlio trovato per il contenuto HTML dell'URL: {}", url);
                                }
                            } catch (IOException e) {
                                logger.error("Errore nel recupero del contenuto HTML per l'URL: {}", url, e);
                            }

                        }
                    } else {
                        logger.warn("Il campo '{}' non è un array nel file JSON: '{}'", field, fileName);
                    }
                }
                logger.info("Lettura degli elementi dal file JSON completata: '{}', campo: '{}'", fileName, field);
            } else {
                logger.warn("Il file JSON '{}' non contiene un array alla radice.", fileName);
            }

        } catch (IOException e) {
            logger.error("Errore durante la lettura degli elementi dal file JSON '{}'", fileName, e);
        }

        return applets;
    }

    private static void saveElementAndNameToJson(Elements elements, String text, String fileName) {
        Map<String, String> textElementMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File(fileName);

        // Converti gli elementi in una stringa HTML e aggiungi alla mappa con il nome
        for (Element e : elements) {
            textElementMap.put(text, e.outerHtml());
        }

        List<Map<String, String>> existingElementsTextList = new ArrayList<>();
        if (file.exists()) {
            try {
                // Leggi il contenuto esistente
                existingElementsTextList = objectMapper.readValue(file, new TypeReference<List<Map<String, String>>>() {});
            } catch (IOException e) {
                logger.warn("Errore durante la lettura del file JSON esistente: {}", fileName, e);
            }
        }

        // Aggiungi il nuovo elemento
        existingElementsTextList.add(textElementMap);

        // Riscrivi il file con i nuovi dati
        try {
            objectMapper.writeValue(file, existingElementsTextList);
            logger.info("Salvato l'elemento nel file JSON: {}", fileName);
        } catch (IOException e) {
            logger.error("Errore durante il salvataggio dell'elemento nel file JSON", e);
        }
    }


    public static Map<String, Elements> readElementsAndNameFromJson(String fileName) {
        Map<String, Elements> applets = new HashMap<>();

        try (InputStream inputStream = JSONParser.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                logger.warn("Il file JSON '{}' non esiste nella cartella resources.", fileName);
                return applets;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);

            if (rootNode.isArray()) {
                for (JsonNode mapNode : rootNode) {
                    if (mapNode.isObject()) {
                        Iterator<Map.Entry<String, JsonNode>> fields = mapNode.fields();
                        while (fields.hasNext()) {
                            Map.Entry<String, JsonNode> field = fields.next();
                            String key = field.getKey();
                            String htmlContent = field.getValue().asText();

                            try {
                                // Parse the HTML content into JSoup Elements
                                Document doc = Jsoup.parse(htmlContent);
                                Elements value = doc.select("div.applet-details");

                                if (value.isEmpty()) {
                                    logger.warn("Nessun elemento trovato per la chiave '{}'.", key);
                                } else {
                                    applets.put(key, value);
                                }
                            } catch (Exception e) {
                                logger.error("Errore nel parsing del contenuto HTML per la chiave '{}': {}", key, e.getMessage());
                            }
                        }
                    } else {
                        logger.warn("Elemento non è un oggetto nel file JSON '{}'", fileName);
                    }
                }
                logger.info("Lettura degli elementi dal file JSON completata: '{}'", fileName);
            } else {
                logger.warn("Il file JSON '{}' non contiene un array alla radice.", fileName);
            }
        } catch (IOException e) {
            logger.error("Errore durante la lettura del file JSON '{}': {}", fileName, e.getMessage());
        }

        logger.info("Numero totale di elementi nel file JSON '{}': {}", fileName, applets.size());
        return applets;
    }
}
