package com.iftttse.dbloader.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Servizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nome;
    @Column(length = 4096) // Modifica la lunghezza della colonna descrizione
    private String descrizione;
    private String url_img;

    public Servizio(String titolo, String urlImg, String descrizione) {
        this.nome = titolo;
        this.url_img = urlImg;
        this.descrizione = descrizione;
    }

    public Servizio() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
