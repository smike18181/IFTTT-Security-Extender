package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Applet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AppletRepository extends JpaRepository<Applet, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Applet a WHERE a.action IN (SELECT act FROM Action act WHERE act.nome IS NULL AND act.descrizione IS NULL) AND a.trigger IN (SELECT trig FROM Triggers trig WHERE trig.nome IS NULL AND trig.descrizione IS NULL)")
    void deleteAppletsWithNullActionAndTrigger();

}
