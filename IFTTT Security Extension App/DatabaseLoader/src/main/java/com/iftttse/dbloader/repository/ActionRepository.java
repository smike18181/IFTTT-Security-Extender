package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Action;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ActionRepository extends JpaRepository<Action, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Action a WHERE a.nome IS NULL AND a.descrizione IS NULL")
    void deleteNullActions();
}
