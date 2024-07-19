package com.iftttse.linkloader.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftttse.linkloader.service.WebScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WebScrapingRunner implements CommandLineRunner {

    @Autowired
    private WebScrapingService webScrapingService;

    @Override
    public void run(String... args) throws Exception {

        JsonFileHandler jsonFileHandler = new JsonFileHandler();

        String linksFileName = "links.json";
        String servicesFileName = "services.json";

        //links per fare estrazione
        String mainLink = "https://ifttt.com/explore/services";
        String baseLink = "https://ifttt.com";

        List<String> links = jsonFileHandler.readStringsFromJsonFile(servicesFileName);
        if(links.isEmpty()){
            links = webScrapingService.
                    extractAndVisitLinks(mainLink, baseLink,
                            ".web-service-cards",
                            ".web-service-card",0);
            jsonFileHandler.saveStringsToJsonFile(links, servicesFileName);
        }

        int lastIndex = jsonFileHandler.getLastLinkDataIndex(linksFileName);

        links = links.stream().skip(lastIndex+1).toList();

        for (String link : links) {
            LinkData ld = new LinkData();

            ld.setLink(link);

            List<String> links_applets = webScrapingService
                    .extractAndVisitLinks(link, baseLink,
                            ".web-applet-cards", ".web-applet-card",0);

            ld.setAppletLinks(links_applets);

            String linkDetails = link+"/details";
            int num_classes = webScrapingService.getSizeElements
                    (linkDetails, ".discover_service_view__tqa-list___-B15q" );

            if(num_classes==0) continue;

            int indexTriggers = webScrapingService.getIndexOfHeaders(linkDetails,
                    ".discover_service_view__header___yBMPF","Triggers");
            int indexActions = webScrapingService.getIndexOfHeaders(linkDetails,
                    ".discover_service_view__header___yBMPF","Actions");


            List<String> links_triggers = webScrapingService
                    .extractAndVisitLinks(linkDetails, baseLink,
                            ".discover_service_view__tqa-list___-B15q",
                            ".web-applet-card",indexTriggers);

            if(!links_triggers.isEmpty()){
                links_triggers.remove(links_triggers.size() - 1);
            }

            ld.setTriggerlinks(links_triggers);

            List<String> links_actions = webScrapingService
                    .extractAndVisitLinks(linkDetails, baseLink,
                            ".discover_service_view__tqa-list___-B15q",
                            ".web-applet-card", indexActions);

            if(!links_actions.isEmpty()) {
                links_actions.remove(links_actions.size() - 1);
            }

            ld.setActionlinks(links_actions);

            jsonFileHandler.appendLinkDataToFile(ld, "links.json");
        }

    }


}
