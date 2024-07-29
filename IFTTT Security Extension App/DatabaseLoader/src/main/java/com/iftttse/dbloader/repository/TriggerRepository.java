package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Triggers;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TriggerRepository  extends JpaRepository<Triggers, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Triggers t WHERE t.nome IS NULL AND t.descrizione IS NULL")
    void deleteNullTriggers();
}
