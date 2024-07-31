package com.iftttse.backend.entity;

import jakarta.persistence.*;

@Entity
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @Column(length = 1024)
    private String descrizione;
    @ManyToOne
    @JoinColumn(name = "canale_id")
    private Servizio canale;

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", canale=" + canale +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

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
