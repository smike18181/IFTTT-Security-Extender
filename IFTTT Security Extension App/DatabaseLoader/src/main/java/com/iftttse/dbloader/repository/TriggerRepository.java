package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriggerRepository  extends JpaRepository<Trigger, Long> {
}
