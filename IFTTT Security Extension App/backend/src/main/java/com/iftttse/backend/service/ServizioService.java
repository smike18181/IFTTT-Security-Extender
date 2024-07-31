package com.iftttse.backend.service;

import com.iftttse.backend.entity.Applet;
import com.iftttse.backend.entity.Servizio;
import com.iftttse.backend.repository.AppletRepository;
import com.iftttse.backend.repository.ServizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServizioService {

    private final ServizioRepository servizioRepository;

    @Autowired
    public ServizioService(ServizioRepository servizioRepository) {
        this.servizioRepository = servizioRepository;
    }

    public List<Servizio> getServicesByName(String name) {
        return servizioRepository.findByNomeContaining(name);
    }

    public List<Servizio> getAllServices() {
        return servizioRepository.findAll();
    }
}
