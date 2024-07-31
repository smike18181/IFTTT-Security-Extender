package com.iftttse.backend.repository;

import com.iftttse.backend.entity.Triggers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriggerRepository  extends JpaRepository<Triggers, Long> {

}
