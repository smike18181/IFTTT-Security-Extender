package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
