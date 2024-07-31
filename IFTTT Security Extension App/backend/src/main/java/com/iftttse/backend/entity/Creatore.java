package com.iftttse.backend.entity;

import jakarta.persistence.*;
@Entity
public class Creatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private boolean by_service_owner;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isBy_service_owner() {
        return by_service_owner;
    }

    public void setBy_service_owner(boolean by_service_owner) {
        this.by_service_owner = by_service_owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
