package com.iftttse.linkloader.runner;

import java.util.List;

public class LinkData {
    private String link;
    private List<String> appletlinks;

    private List<String> triggerlinks;

    private List<String> actionlinks;

    // Costruttore
    public LinkData(String link, List<String> links) {
        this.link = link;
        this.appletlinks = links;
    }

    public LinkData(){}

    // Getters e Setters
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getAppletLinks() {
        return appletlinks;
    }

    public void setAppletLinks(List<String> links) {
        this.appletlinks = links;
    }

    public List<String> getTriggerlinks() {
        return triggerlinks;
    }

    public List<String> getAppletlinks() {
        return appletlinks;
    }

    public void setAppletlinks(List<String> appletlinks) {
        this.appletlinks = appletlinks;
    }

    public List<String> getActionlinks() {
        return actionlinks;
    }

    public void setActionlinks(List<String> actionlinks) {
        this.actionlinks = actionlinks;
    }

    public void setTriggerlinks(List<String> triggerlinks) {
        this.triggerlinks = triggerlinks;
    }
}


