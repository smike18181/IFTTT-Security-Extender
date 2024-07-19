package com.iftttse.dbloader.Entity;

import jakarta.persistence.*;

@Entity
public class Applet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int installs_count;
    private int num_services = 2;
    private boolean required_android_app;
    private boolean required_ios_app;
    @OneToOne
    private Creatore creator;
    @OneToOne
    private Trigger trigger;
    @OneToOne
    private Action action;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getInstalls_count() {
        return installs_count;
    }

    public void setInstalls_count(int installs_count) {
        this.installs_count = installs_count;
    }

    public int getNum_services() {
        return num_services;
    }

    public void setNum_services(int num_services) {
        this.num_services = num_services;
    }

    public Creatore getCreator() {
        return creator;
    }

    public void setCreator(Creatore creator) {
        this.creator = creator;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isRequired_android_app() {
        return required_android_app;
    }

    public void setRequired_android_app(boolean required_android_app) {
        this.required_android_app = required_android_app;
    }

    public boolean isRequired_ios_app() {
        return required_ios_app;
    }

    public void setRequired_ios_app(boolean required_ios_app) {
        this.required_ios_app = required_ios_app;
    }
}
