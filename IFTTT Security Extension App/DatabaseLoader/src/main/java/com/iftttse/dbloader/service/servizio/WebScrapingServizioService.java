package com.iftttse.dbloader.service.servizio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class WebScrapingServizioService {

    @Autowired
    private Elements serviceCards;

    private List<Document>services_pages;

    public String getTitle(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.title();
    }

    public List<Document> loadDocuments(List<String>urls) throws IOException {

        List<Document>documents = new ArrayList<>();

        for(String url : urls){
            documents.add(Jsoup.connect(url).get());
        }

        return documents;
    }

    public List<String> extractServiceCardTitles(){
        List<String> titles = new ArrayList<>();

        System.out.println("------ESTRAZIONE TITOLI DEI SERVIZI------");
        for (Element card : serviceCards) {

            Element titleElement = card.selectFirst("span.service_card__service-card-title___tWghL");

                if (titleElement != null) {
                    System.out.println("titolo: "+titleElement.text());
                    // Aggiungi il testo del titolo alla lista
                    titles.add(titleElement.text());
                }
        }

        System.out.println(titles.size());
        return titles;
    }

    public List<String> extractServiceCardUrlImg() {
        List<String> urls = new ArrayList<>();

        System.out.println("------ESTRAZIONE LOGHI DEI SERVIZI------");
        for (Element card : serviceCards) {

            Element imgs = card.selectFirst("img");

            if (imgs != null) {
                System.out.println("Url loghi: "+imgs.attr("src"));
                urls.add(imgs.attr("src"));
            }
        }

        System.out.println(urls.size());
        return urls;
    }

    public List<String> extractServiceDescriptions(List<String> urls) throws IOException {

        System.out.println("------ESTRAZIONE DESCRIZIONE DEI SERVIZI------");
        List<String>descriptions = new ArrayList<>();

        if(services_pages==null || services_pages.isEmpty())
            services_pages = loadDocuments(urls);

        System.out.println(services_pages.size());

        for(Document service_page : services_pages){

            Element descriptionContainer = service_page.selectFirst(".service-description");

            if (descriptionContainer != null) {
                // Seleziona il tag <p> all'interno del div e ottieni il testo
                Element descriptionElement = descriptionContainer.selectFirst("p");
                if (descriptionElement != null) {
                    // Aggiungi il testo della descrizione alla lista
                    System.out.println("descrizione : "+descriptionElement.text());
                    descriptions.add(descriptionElement.text());
                }
            }
        }

        return descriptions;
    }

    public List<String> loadServicesLinks(String resourcePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> links = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }
            // Leggi il file JSON e mappalo a una lista di stringhe
            links = objectMapper.readValue(inputStream, new TypeReference<List<String>>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return links;
    }

}
