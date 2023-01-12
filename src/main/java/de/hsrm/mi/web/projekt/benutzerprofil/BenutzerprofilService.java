package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Optional;

import de.hsrm.mi.web.projekt.angebot.Angebot;

public interface BenutzerprofilService {
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp);
    public Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id);
    public List<BenutzerProfil> alleBenutzerProfile();
    public void loescheBenutzerProfilMitId(Long loesch);
    public void fuegeAngebotHinzu(Long id, Angebot angebot);
    public void loescheAngebot(Long id);
    public List<Angebot> alleAngebote();
    public Optional<Angebot> findeAngebotMitID(long angebotid);
}
