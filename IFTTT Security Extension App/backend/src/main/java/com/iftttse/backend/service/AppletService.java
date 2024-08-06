package com.iftttse.backend.service;

import com.iftttse.backend.entity.Applet;
import com.iftttse.backend.repository.AppletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AppletService {

    private final AppletRepository appletRepository;

    @Autowired
    public AppletService(AppletRepository appletRepository) {
        this.appletRepository = appletRepository;
    }

    public List<Applet> getAppletsByName(String name) {
        return appletRepository.findByNomeContaining(name);
    }

    public List<Applet> getAppletsByCanaleId(Long canaleId) {
        List<Applet> appletsFromActions = appletRepository.findByAction_Canale_Id(canaleId);
        List<Applet> appletsFromTriggers = appletRepository.findByTrigger_Canale_Id(canaleId);

        // Utilizzare un Set per evitare duplicati
        Set<Applet> appletSet = new HashSet<>(appletsFromActions);
        appletSet.addAll(appletsFromTriggers);

        return List.copyOf(appletSet); // Convertire di nuovo in List se necessario
    }

    public Applet getAppletsById(Long appletId) {

        Optional<Applet> optional = appletRepository.findById(appletId);
        if(optional.isPresent())
            return optional.get();
        return null;

    }
}
