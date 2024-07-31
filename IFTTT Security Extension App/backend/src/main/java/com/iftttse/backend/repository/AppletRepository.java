package com.iftttse.backend.repository;

import com.iftttse.backend.entity.Applet;
import com.iftttse.backend.entity.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppletRepository extends JpaRepository<Applet, Long> {
    List<Applet> findByNomeContaining(String nome);
    List<Applet> findByAction_Canale_Id(Long canaleId);
    List<Applet> findByTrigger_Canale_Id(Long triggerId);

}
