package com.iftttse.dbloader.component;

import com.iftttse.dbloader.Entity.Action;
import com.iftttse.dbloader.Entity.Applet;
import com.iftttse.dbloader.Entity.Servizio;
import com.iftttse.dbloader.Entity.Triggers;
import com.iftttse.dbloader.repository.*;
import com.iftttse.dbloader.service.applet.AppletLoader;
import com.iftttse.dbloader.service.applet.WebScrapingAppletService;
import com.iftttse.dbloader.service.servizio.WebScrapingServizioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class WebScrapingRunner implements CommandLineRunner {

    @Autowired
    private WebScrapingServizioService webScrapingService;

    @Autowired
    private WebScrapingAppletService webScrapingApplet;

    @Autowired
    private ServizioRepository servizioRepository;

    @Autowired
    private AppletRepository appletRepository;

    private static final Logger logger = LoggerFactory.getLogger(WebScrapingRunner.class);

    private final String MainLink = "https://ifttt.com/explore/services";
    private final String BaseLink = "https://ifttt.com";
    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private CreatoreRepository creatoreRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Servizio> servizi;
        List<List<Applet>> applets;

        if (args.length > 0) {
            switch (args[0]) {
                case "SERVICES": {
                    servizi = loadServiceObjects();
                    servizioRepository.saveAll(servizi);
                    logger.info("Servizi salvati con successo.");
                    break;
                }
                case "APPLETS": {
                    applets = loadAppletObjects();
                    for (List<Applet> appl : applets) {
                        for(Applet app : appl){
                            actionRepository.save(app.getAction());
                            triggerRepository.save(app.getTrigger());
                            creatoreRepository.save(app.getCreator());
                        }
                        appletRepository.saveAll(appl);
                    }
                    logger.info("Applets salvati con successo.");
                    break;
                }
                case "DETAILS": {
                    /*List<Applet> appl = appletRepository.findAll();
                    updateApplets(appl);

                    logger.info("PRONTO PER IL CARICAMENTO NEL DB");

                    for (Applet applet : appl) {

                        Action action = applet.getAction();
                        if (action != null) {
                            actionRepository.save(action);
                        }

                        Triggers triggers = applet.getTrigger();
                        if (triggers != null) {
                            triggerRepository.save(triggers);
                        }

                        appletRepository.save(applet);
                    }

                    logger.info("APPLET AGGIORNATE CON SUCCESSO");*/

                    logger.info("RIMOZIONE APPLET NON AGGIORNATE");
                    appletRepository.deleteAppletsWithNullActionAndTrigger();

                    logger.info("RIMOZIONE ACTION E TRIGGER NON AGGIONRATE");
                    actionRepository.deleteNullActions();
                    triggerRepository.deleteNullTriggers();

                    break;
                }
                default: {
                    logger.warn("Argomento non riconosciuto: " + args[0]);
                    break;
                }
            }
        } else {
            logger.error("Nessun argomento fornito.");
        }
    }

    public List<Servizio> loadServiceObjects() throws IOException {
        List<Servizio> servizi = new ArrayList<>();

        List<String> titoli = webScrapingService.extractServiceCardTitles();
        List<String> urls_img = webScrapingService.extractServiceCardUrlImg();
        List<String> links = webScrapingService.loadServicesLinks("services.json");
        List<String> descriptions = webScrapingService.extractServiceDescriptions(links);

        for (int i = 0; i < links.size(); i++) {
            String titolo = titoli.get(i);
            String urlImg = urls_img.get(i);
            String descrizione = descriptions.get(i);

            Servizio servizio = new Servizio(titolo, urlImg, descrizione);
            servizi.add(servizio);
        }

        logger.debug("Servizi caricati: {}", servizi);
        return servizi;
    }

    public List<List<Applet>> loadAppletObjects() throws IOException {
        logger.info("----CARICAMENTO APPLET AVVIATO----");
        logger.info("----Primo Caricamento----");
        List<List<Applet>> allApplets = webScrapingApplet.extractInfoOfAppletsMainPage();
        AppletLoader loader = new AppletLoader();

        List<List<String>> allAppletlinks = loader.loadAppletsLinks("links.json");

        logger.debug("Applets caricati: {}", allApplets);
        return allApplets;
    }

    public void updateApplets(List<Applet> appl) throws IOException {
        logger.info("----CARICAMENTO DETTAGLI APPLET AVVIATO----");

        // Logging iniziale per vedere lo stato prima dell'aggiornamento
        for (Applet a : appl) {
            logger.info("Initial Applet state: " + a);
        }

        webScrapingApplet.completeAppletLoading(appl);

        // Logging finale per vedere lo stato dopo l'aggiornamento
        for (Applet a : appl) {
            logger.info("Updated Applet state: " + a);
        }
    }
}
