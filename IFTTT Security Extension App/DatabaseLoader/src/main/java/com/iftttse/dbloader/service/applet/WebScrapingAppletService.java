package com.iftttse.dbloader.service.applet;

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
import java.util.*;

@Service
public class WebScrapingAppletService {

    @Autowired
    @Qualifier("appletsCardsBean")
    private List<Elements> appletsCards;

    @Autowired
    @Qualifier("appletsDetails")
    private Map<String, Elements> appletsDetails;

    @Autowired
    private ServizioRepository servizioRepository;

    public List<List<Applet>> extractInfoOfAppletsMainPage() {
        List<List<Applet>> allApplet = new ArrayList<>();

        for (Elements elements : appletsCards) {

            List<Applet> applets = new ArrayList<>();
            int counter = 0;

            for (Element element : elements) {

                Applet applet = new Applet();

                String name = addNameinApplets(element);
                counter++;
                System.out.println("APPLET "+name+" APPLET "+counter);

                applet.setNome(name);
                applet.setInstalls_count(addInstallCountsinApplets(element));

                Triggers triggers = new Triggers();
                Servizio canale1 = addChannelOfApplet(1, element);
                if(canale1==null) continue;

                triggers.setCanale(canale1);

                Action action = new Action();
                Servizio canale2 = addChannelOfApplet(2, element);
                if(canale2==null) continue;

                action.setCanale(canale2);

                Creatore creatore = new Creatore();
                addInfoOfCreatore(creatore, element,
                        triggers.getCanale().getNome(),
                        action.getCanale().getNome());

                applet.setAction(action);
                applet.setTrigger(triggers);
                applet.setCreator(creatore);

                applets.add(applet);
            }
            allApplet.add(applets);
        }

        return allApplet;
    }

    public String addNameinApplets(Element element) {
        Element titleElement = element.selectFirst(".title ");
        Element lastDiv = titleElement.select("div").last();

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
            return 0;
        }

        return 0;
    }

    public Servizio addChannelOfApplet(int channel, Element element) {
        Elements channelsDiv = element.select(".meta-header");

        if(channelsDiv.size()==1)  channel=1;

        Elements img = channelsDiv.select("img");
        String channelName = img.get(channel - 1).attr("title");

        if (channelName.equals("Philips Hue Turn off lights.")) {
            channelName = "Philips Hue";
        }
        if (channelName.equals("NZXT CAM")) {
            channelName = "Active Sleep";
        }


        System.err.println(channelName);
        return servizioRepository.getServizioByNome(channelName.trim());
    }


    public void addInfoOfCreatore(Creatore creatore, Element element,
                                  String ownerTrigger, String ownerAction) {
        Element span = element.selectFirst(".author");
        if(span==null){
            span = new Element(ownerTrigger);
        }
        creatore.setNome(span.text());

        creatore.setBy_service_owner(
                span.text().contains(ownerTrigger) || span.text().contains(ownerAction));
    }

    public void completeAppletLoading(List<Applet> applets) throws IOException {
        for (Applet a : applets) {
            String key = a.getNome();
            Elements element = appletsDetails.get(key);

            if(element != null)
                  completeAppletsInfo(a, element);
        }
    }


    private void completeAppletsInfo(Applet applet, Elements element) {
        Action action = applet.getAction();
        Triggers triggers = applet.getTrigger();


        action.setNome(completeInfoActionOrTrigger("ACTION", "NAME", element));
        triggers.setNome(completeInfoActionOrTrigger("TRIGGER", "NAME", element));

        action.setDescrizione(completeInfoActionOrTrigger("ACTION", "DESCRIZIONE", element));
        triggers.setDescrizione(completeInfoActionOrTrigger("TRIGGER", "DESCRIZIONE", element));

        applet.setAction(action);
        applet.setTrigger(triggers);

        addMobileInformations(applet);
    }

    private void addMobileInformations(Applet applet) {
        String appletName = applet.getNome();
        Action action = applet.getAction();
        Triggers trigger = applet.getTrigger();

        String actionName = action.getNome();
        String actionDescription = action.getDescrizione();

        String triggerName = trigger.getNome();
        String triggerDescription = trigger.getDescrizione();

        List<String> androidKeywords = Arrays.asList(
                "Android", "APK", "Google Play", "Samsung", "Huawei", "Xiaomi",
                "Pixel", "OnePlus", "Oppo", "Vivo", "Realme", "Sony Xperia",
                "LG", "Motorola", "Nokia", "Android Auto", "Wear OS", "Google Assistant",
                "Samsung Galaxy", "Galaxy Store", "Play Services", "Google Maps",
                "Google Home", "Google Now", "AOSP", "Material Design", "Google Photos",
                "Google Drive", "Fast Charging", "Wireless Charging", "Google Lens",
                "Android TV", "Android Tablet", "Samsung DeX", "One UI", "EMUI",
                "MIUI", "ColorOS", "OxygenOS"
        );

        List<String> iosKeywords = Arrays.asList(
                "iOS", "iPhone", "App Store", "Apple", "iPad", "iPod", "Apple Watch",
                "Apple TV", "iMessage", "Siri", "iCloud", "iTunes", "AirPods", "HomePod",
                "FaceTime", "iOS Update", "macOS", "Apple Music", "Apple Pay", "Apple Maps",
                "iOS SDK", "ARKit", "Handoff", "iOS App Clip", "iPadOS", "tvOS",
                "watchOS", "Retina Display", "Super Retina XDR", "Touch ID", "Face ID",
                "Apple Arcade", "Apple News+", "Apple Card", "AirPlay", "CarPlay",
                "iCloud Drive", "ProMotion", "LiDAR Scanner", "Airdrop"
        );

        List<String> mobileKeywords = Arrays.asList(
                "Windows Phone", "BlackBerry", "Symbian", "Firefox OS", "Ubuntu Touch",
                "5G", "4G LTE", "Bluetooth", "NFC", "Wi-Fi", "GPS", "Dual SIM", "VoLTE",
                "USB-C", "AMOLED", "OLED", "LCD", "HDR", "Dolby Vision", "IP68",
                "Gorilla Glass", "eSIM", "Nano SIM", "Micro SIM", "Voice Assistant",
                "Gesture Navigation", "Dark Mode", "Virtual Reality", "Mobile Security",
                "Privacy Settings", "Data Encryption", "Mobile Payments", "Mobile Wallet",
                "Contactless Payment", "QR Code", "Mobile Hotspot", "Tethering", "Battery Life",
                "Camera", "Megapixel", "Night Mode", "Portrait Mode", "Video Recording",
                "4K", "1080p", "Slow Motion", "Time-Lapse", "Mobile Photography",
                "Selfie", "Dual Camera", "Triple Camera", "Quad Camera", "5G Ready",
                "Unlocked Phone", "Carrier", "SIM Free", "Smartphone", "Tablet",
                "Smartwatch", "Fitness Tracker", "Portable Charger", "Power Bank",
                "Phone Case", "Screen Protector", "Stylus", "Mobile Gaming", "App Development"
        );

        boolean isMobile =
                containsAnyKeyword(mobileKeywords, appletName, actionName, actionDescription, triggerName, triggerDescription);
        boolean isAndroid =
                containsAnyKeyword(androidKeywords, appletName, actionName, actionDescription, triggerName, triggerDescription);
        boolean isIOS =
                containsAnyKeyword(iosKeywords, appletName, actionName, actionDescription, triggerName, triggerDescription);

        if (isAndroid || isMobile) {
            applet.setRequired_android_app(true);
        }

        if (isIOS || isMobile) {
            applet.setRequired_ios_app(true);
        }
    }

    // Funzione di supporto per verificare la presenza di parole chiave
    private boolean containsAnyKeyword(List<String> keywords, String... texts) {
        for (String text : texts) {
            for (String keyword : keywords) {
                if (text != null && text.toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }


    private String completeInfoActionOrTrigger(String component, String info, Elements elements) {
        int index = (component.equals("TRIGGER")) ? 0 : 1;
        String className = (info.equals("NAME")) ? ".service-name" : ".txt-caption";

        return elements.select(className)
                .get(index).select("a").text();
    }

}
