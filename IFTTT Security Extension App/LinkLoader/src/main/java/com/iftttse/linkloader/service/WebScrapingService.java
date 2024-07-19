package com.iftttse.linkloader.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jsoup.nodes.Entities.EscapeMode.base;

@Service
public class WebScrapingService {

    public String getTitle(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.title();
    }

    public int getSizeElements(String url, String listContainer) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.select(listContainer).size();
    }

    public int getIndexOfHeaders(String url, String listContainer, String headerTitle) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements headers = document.select(listContainer);

        for(Element header : headers){
             if(header.text().equalsIgnoreCase(headerTitle)){
                 return headers.indexOf(header);
             }
        }
        return -1;
    }

    public List<String> extractAndVisitLinks(String url, String base_url,
                                             String listContainer,
                                             String fieldContainer,
                                             int index ) throws IOException {
        if(index<0)  return new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        List<String> links = new ArrayList<>();

        Elements containers = document.select(listContainer);
        Element container = containers.get(index);

        if (container != null) {

            Elements serviceCards = container.select(fieldContainer);
            System.out.print(serviceCards.size());

            for (Element card : serviceCards) {

                Element linkElement = card.selectFirst("a");
                if (linkElement != null) {
                    java.lang.String linkHref = linkElement.attr("href");

                    java.lang.String absoluteLink = linkHref.startsWith("http") ? linkHref : base_url + linkHref;

                    links.add(absoluteLink);

                    // Estrai il titolo della pagina collegata
                    java.lang.String linkedPageTitle = getTitle(absoluteLink);
                    System.out.println("Titolo della pagina collegata a " + absoluteLink + ": " + linkedPageTitle);

                }
            }
        } else {
            System.out.println("Nessun elemento con classe 'web-service-cards' trovato.");
        }

        return links;
    }

}
