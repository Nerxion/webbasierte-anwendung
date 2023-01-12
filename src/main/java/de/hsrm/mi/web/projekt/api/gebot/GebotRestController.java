package de.hsrm.mi.web.projekt.api.gebot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.gebot.GebotRepository;
import de.hsrm.mi.web.projekt.gebot.GebotServiceImpl;

@RestController
@RequestMapping("/api/gebot")
public class GebotRestController {
    @Autowired private GebotRepository gebotsrepo;
    @Autowired private GebotServiceImpl gebotService;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetGebotResponseDTO> gebot_get() {
        List<Gebot> alleGebote = gebotsrepo.findAll();
        List<GetGebotResponseDTO> dtoAlleGebote = new ArrayList<GetGebotResponseDTO>();
        for (int i = 0; i < alleGebote.size(); i++) {
            dtoAlleGebote.add(GetGebotResponseDTO.from(alleGebote.get(i)));
        }
        return dtoAlleGebote;
    }

    @PostMapping()
    public long gebot_post(WebClient webclient) { // ???? JSON-Objekt.... was. Und muss das auch JSON producen wie das GetMapping?
        AddGebotRequestDTO gebotDTO = webclient.post().retrieve().bodyToMono(AddGebotRequestDTO.class).block();
        Gebot gebot = gebotService.bieteFuerAngebot(gebotDTO.benutzerprofilid(), gebotDTO.angebotid(), gebotDTO.betrag());
        return gebot.getId();
    }

    @DeleteMapping("/{id}")
    public void gebot_del(@PathVariable("id") long id) {
        gebotService.loescheGebot(id);
    }
}
