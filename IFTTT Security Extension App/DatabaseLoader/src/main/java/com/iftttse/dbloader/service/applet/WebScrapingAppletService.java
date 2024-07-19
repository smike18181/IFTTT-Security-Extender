package com.iftttse.dbloader.service.applet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftttse.dbloader.Entity.*;
import com.iftttse.dbloader.repository.ServizioRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WebScrapingAppletService {

    @Autowired
    @Qualifier("appletsCardsBean")
    private List<Elements> appletsCards;

    @Autowired
    private ServizioRepository servizioRepository;

    public List<List<Applet>> extractInfoOfAppletsMainPage() {
        List<List<Applet>> allApplet = new ArrayList<>();

        int counter = 0;
        for (Elements elements : appletsCards) {
            List<Applet> applets = new ArrayList<>();
            for (Element element : elements) {

                Applet applet = new Applet();
                counter++;

                applet.setNome(addNameinApplets(element));
                applet.setInstalls_count(addInstallCountsinApplets(element));

                Action action = new Action();
                action.setCanale(addChannelOfApplet(2, element));

                Trigger trigger = new Trigger();
                trigger.setCanale(addChannelOfApplet(1, element));

                Creatore creatore = new Creatore();
                addInfoOfCreatore(creatore, element,
                        trigger.getCanale().getNome(),
                        action.getCanale().getNome());

                applets.add(applet);
            }
            allApplet.add(applets);
        }

        return allApplet;
    }

    public String addNameinApplets(Element element) {
        Element lastDiv = element.select("div").last();

        if (lastDiv != null) {
            return lastDiv.text();
        }
        return null;
    }

    public Integer addInstallCountsinApplets(Element element) {
        Element lastDiv = element.selectFirst(".meta");

        if (lastDiv != null) {
            Elements spans = lastDiv.select("span");
            if (!spans.isEmpty()) {
                String lastSpanText = Objects.requireNonNull(spans.last()).text();

                try {
                    return Integer.parseInt(lastSpanText);
                } catch (NumberFormatException e) {
                    // Handle the exception if needed
                }
            }
        }

        return null;
    }

    public Servizio addChannelOfApplet(int channel, Element element) {
        Elements channelsDiv = element.select(".meta-header");
        Elements img = channelsDiv.select("img");
        String channelName = img.get(channel - 1).attr("title");
        return servizioRepository.getServizioByNome(channelName.trim());
    }


    public void addInfoOfCreatore(Creatore creatore, Element element,
                                  String ownerTrigger, String ownerAction) {
        Element span = element.selectFirst(".author");
        creatore.setNome(span.text());

        creatore.setBy_service_owner(
                span.text().contains(ownerTrigger) || span.text().contains(ownerAction));
    }

    public void completeAppletLoading(List<List<Applet>> allApplets,
                                      List<List<String>> allAppletlinks) throws IOException {

        for (int i = 0; i < allAppletlinks.size(); i++) {
            List<String> appletLinks = allAppletlinks.get(i);
            List<Applet> applets = allApplets.get(i);

            for (int j = 0; j < appletLinks.size(); j++) {
                Applet applet = applets.get(j);
                String link = appletLinks.get(j);

                try {
                    Element element = loadElementFromDocument(link);
                    completeAppletsInfo(applet, element);
                } catch (IOException e) {
                    // Handle the exception if needed
                }
            }
        }
    }

    private void completeAppletsInfo(Applet applet, Element element) {
        Action action = applet.getAction();
        Trigger trigger = applet.getTrigger();

        action.setNome(completeInfoActionOrTrigger("ACTION", "NAME", element));
        trigger.setNome(completeInfoActionOrTrigger("TRIGGER", "NAME", element));

        action.setDescrizione(completeInfoActionOrTrigger("ACTION", "DESCRIZIONE", element));
        trigger.setDescrizione(completeInfoActionOrTrigger("TRIGGER", "DESCRIZIONE", element));

        applet.setAction(action);
        applet.setTrigger(trigger);
    }

    private String completeInfoActionOrTrigger(String component,
                                               String info, Element element) {
        int index = (component.equals("TRIGGER")) ? 0 : 1;
        String className = (info.equals("NAME")) ? "service-name" : "txt-caption";

        return Objects.requireNonNull(element.select(className)
                .get(index).selectFirst("a")).text();
    }

    public Element loadElementFromDocument(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        return Objects.requireNonNull(
                doc.selectFirst(".applet-details")).selectFirst(".details");
    }
}
