package com.iftttse.backend.entity;
import jakarta.persistence.*;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }
}
