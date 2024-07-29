package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    Servizio getServizioByNome(String nome);
}
