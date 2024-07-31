package com.iftttse.backend.repository;

import com.iftttse.backend.entity.Applet;
import com.iftttse.backend.entity.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    Servizio getServizioByNome(String nome);
    List<Servizio> findByNomeContaining(String nome);
}
