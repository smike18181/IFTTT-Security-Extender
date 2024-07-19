package com.iftttse.dbloader.Entity;

import jakarta.persistence.*;

@Entity
public class Trigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;
    @OneToOne
    private Servizio canale;

    public Servizio getCanale() {
        return canale;
    }

    public void setCanale(Servizio canale) {
        this.canale = canale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
