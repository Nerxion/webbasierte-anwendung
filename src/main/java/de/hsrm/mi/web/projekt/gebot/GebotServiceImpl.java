package de.hsrm.mi.web.projekt.gebot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendInfoServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;

@Service
public class GebotServiceImpl implements GebotService{
    @Autowired private GebotRepository gebotsrepo;
    @Autowired private BenutzerprofilServiceImpl bpService;
    @Autowired private BackendInfoServiceImpl backendinfoservice;

    @Override
    public List<Gebot> findeAlleGebote() {
        List<Gebot> alleGebote = gebotsrepo.findAll();
        return alleGebote;
    }

    @Override
    public List<Gebot> findeAlleGeboteFuerAngebot(long angebotid) {
        Optional<Angebot> mangebot = bpService.findeAngebotMitID(angebotid);
        if (!mangebot.isEmpty()) {
            Angebot angebot = mangebot.get();
            List<Gebot> gebote = angebot.getGebote();
            return gebote;
        } else {
            return null;
        }
    }

    @Override
    public Gebot bieteFuerAngebot(long benutzerprofilid, long angebotid, long betrag) {
        Optional<Gebot> existGebot = gebotsrepo.findByAngebotIdAndBieterId(angebotid, benutzerprofilid);
        if (existGebot.isEmpty()) {
            Gebot gebot = new Gebot();
            Optional<Angebot> optAngebot = bpService.findeAngebotMitID(angebotid);
            Angebot angebot = optAngebot.get();
            gebot.setAngebot(angebot);
            gebot.setBetrag(betrag);
            Optional<BenutzerProfil> optGebieter = bpService.holeBenutzerProfilMitId(benutzerprofilid);
            BenutzerProfil gebieter = optGebieter.get();
            gebot.setGebieter(gebieter);
            gebot.setGebotzeitpunkt(LocalDateTime.now());
            gebieter.addGebot(gebot);
            angebot.addGebot(gebot);
            gebotsrepo.save(gebot);
            GetGebotResponseDTO gebotdto = GetGebotResponseDTO.from(gebot);
            backendinfoservice.sendInfo("/gebot/"+gebotdto.angebotid(), BackendOperation.CREATE, gebotdto.angebotid());
            return gebot;
        } else {
            Gebot gebot = existGebot.get();
            gebot.setBetrag(betrag);
            gebot.setGebotzeitpunkt(LocalDateTime.now());
            gebotsrepo.save(gebot);
            GetGebotResponseDTO gebotdto = GetGebotResponseDTO.from(gebot);
            backendinfoservice.sendInfo("/gebot/"+gebotdto.angebotid(), BackendOperation.CREATE, gebotdto.angebotid());
            return gebot;
        }
    }

    @Override
    public void loescheGebot(long gebotid) {
        Optional<Gebot> optGebot = gebotsrepo.findById(gebotid);
        if (!optGebot.isEmpty()) {
            Gebot gebot = optGebot.get();
            Optional<Angebot> optAngebot = bpService.findeAngebotMitID(gebot.getAngebot().getId());
            Angebot angebot = optAngebot.get();
            Optional<BenutzerProfil> optGebieter = bpService.holeBenutzerProfilMitId(gebot.getGebieter().getId());
            BenutzerProfil gebieter = optGebieter.get();
            angebot.removeGebot(gebot);
            gebieter.removeGebot(gebot);
            gebotsrepo.deleteById(gebotid);
        }
    }
    
}
