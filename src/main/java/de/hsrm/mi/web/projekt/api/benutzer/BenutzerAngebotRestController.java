package de.hsrm.mi.web.projekt.api.benutzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;
import de.hsrm.mi.web.projekt.gebot.Gebot;

@RestController
@RequestMapping("/api")
public class BenutzerAngebotRestController {
    @Autowired private AngebotRepository angebotsrepo;
    @Autowired private BenutzerprofilRepository bprepo;
    
    @GetMapping(value = "/angebot", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetAngebotResponseDTO> angebotListe_get() {
        List<Angebot> angebotsListe = angebotsrepo.findAll();
        List<GetAngebotResponseDTO> dtoListe = new ArrayList<GetAngebotResponseDTO>();
        for (int i = 0; i < angebotsListe.size(); i++) {
            dtoListe.add(GetAngebotResponseDTO.from(angebotsListe.get(i)));
        }
        return dtoListe;
    }

    @GetMapping("/angebot/{id}")
    public GetAngebotResponseDTO angebotWithId_get(@PathVariable("id") long id) {
        Optional<Angebot> optAngebot = angebotsrepo.findById(id);
        if (!optAngebot.isEmpty()){
            Angebot angebot = optAngebot.get();
            GetAngebotResponseDTO angebotDTO = GetAngebotResponseDTO.from(angebot);
            return angebotDTO;
        } else {
            return null;
        }
    }

    @GetMapping("/angebot/{id}/gebot")
    public List<GetGebotResponseDTO> angebotAlleGebote_get(@PathVariable("id") long id) {
        Optional<Angebot> optAngebot = angebotsrepo.findById(id);
        if (!optAngebot.isEmpty()){
            Angebot angebot = optAngebot.get();
            List<Gebot> gebotListe = angebot.getGebote();
            List<GetGebotResponseDTO> dtoListe = new ArrayList<GetGebotResponseDTO>();
            for (int i = 0; i < gebotListe.size(); i++) {
                dtoListe.add(GetGebotResponseDTO.from(gebotListe.get(i)));
            }
            return dtoListe;
        } else {
            return null;
        }
    }

    @GetMapping("/benutzer/{id}/angebot")
    public List<GetAngebotResponseDTO> angebotPerBp_get(@PathVariable("id") long id) {
        Optional<BenutzerProfil> optBp = bprepo.findById(id);
        if (!optBp.isEmpty()){
            BenutzerProfil bp = optBp.get();
            List<Angebot> angeboteListe = bp.getAngebote();
            List<GetAngebotResponseDTO> dtoListe = new ArrayList<GetAngebotResponseDTO>();
            for (int i = 0; i < angeboteListe.size(); i++) {
                dtoListe.add(GetAngebotResponseDTO.from(angeboteListe.get(i)));
            }
            return dtoListe;
        } else {
            return null;
        }
    }
}
