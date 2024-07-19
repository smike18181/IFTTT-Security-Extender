package com.iftttse.dbloader.repository;

import com.iftttse.dbloader.Entity.Applet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppletRepository extends JpaRepository<Applet, Long> {
}
