package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    // Metodo per trovare Servizio in cui il nome contiene il parametro "nome"
    @Query("SELECT s FROM Servizio s WHERE s.nome LIKE %:nome%")
    Servizio getServizioByNome(@Param("nome") String nome);
}
