package com.iftttse.dbloader.service.servizio;

import com.iftttse.dbloader.utils.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jsoup.select.Selector.selectFirst;

@Configuration
public class DocumentsConfig {

    private static final Logger logger = LoggerFactory.getLogger(DocumentsConfig.class);

    private Document document;
    private List<Document> documents = new ArrayList<>();
    private WebScrapingServizioService webScrapingService = new WebScrapingServizioService();

    public Document serviceDocument(String url) throws IOException {
        logger.info("Connessione all'URL: {}", url);
        document = Jsoup.connect(url).get();
        logger.debug("Documento caricato da URL: {}", url);
        return document;
    }

    @Bean
    public Elements serviceCards() throws IOException {
        if (document == null) {
            logger.debug("Documento nullo, caricamento del documento di servizio");
            serviceDocument("https://ifttt.com/explore/services");
        }
        Element container = document.selectFirst(".web-service-cards");

        if (container != null) {
            logger.debug("Elemento con classe 'web-service-cards' trovato");
            // Seleziona tutti i div con la classe "web-service-card" all'interno del contenitore
            return container.select(".web-service-card");
        } else {
            logger.warn("Nessun elemento con classe 'web-service-cards' trovato.");
            return null;
        }
    }

    @Bean(name = "appletsCardsBean")
    public List<Elements> AppletsCards(){
        List<Elements> applets = JSONParser.readElementsFromJson("applets.json");

        if(applets == null) {

            applets = new ArrayList<>();

            if (documents.isEmpty()) {
                logger.debug("Lista di documenti vuota, caricamento dei link dei servizi");
                List<String> links = webScrapingService.loadServicesLinks("services.json");
                for (String link : links) {
                    try {
                        documents.add(serviceDocument(link));
                        logger.debug("Documento aggiunto alla lista dei documenti: {}", link);
                    } catch (IOException e) {
                        logger.error("Errore durante il caricamento del documento da link: {}", link, e);
                    }
                }
            }

            for (Document doc : documents) {
                Elements elements = doc.select(".web-applet-card");
                applets.add(elements);
                logger.debug("Aggiunti {} elementi alla lista degli applet", elements.size());
            }

            JSONParser.saveElementsToJson(applets, "applets.json");
        }

        logger.info("Caricamento completo degli applet: {} gruppi di elementi caricati", applets.size());
        return applets;
    }

    @Bean(name = "appletsDetails")
    public Map<String, Elements> AppletsDetails(){
       Map<String, Elements> applets =
               JSONParser.readElementsAndNameFromJson("details.json");

       if(applets == null) {
           logger.info("applets è nulla");
           applets = JSONParser.readElementsFromJson("links.json","appletlinks");
       }

        logger.info("Caricamento completo degli applet: " +
                "{} gruppi di elementi caricati", applets.size());

        return applets;
    }


}
