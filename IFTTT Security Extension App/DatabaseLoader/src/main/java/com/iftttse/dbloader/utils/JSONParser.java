package com.iftttse.dbloader.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
}
