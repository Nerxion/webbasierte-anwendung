package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.geo.AdressInfo;
import de.hsrm.mi.web.projekt.geo.GeoServiceImpl;

@Service
public class BenutzerprofilServiceImpl implements BenutzerprofilService {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilServiceImpl.class);
    @Autowired private BenutzerprofilRepository benutzerprofilrepo;
    @Autowired private GeoServiceImpl geoservice;
    @Autowired private AngebotRepository angebotsrepo;

    @Override
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp) {
        double lat, lon;
        List<AdressInfo> adressInfos = geoservice.findeAdressInfo(bp.getAdresse());
        logger.info("adressInfos = {}", adressInfos);
        if (!adressInfos.isEmpty()) { 
            lat = adressInfos.get(0).lat();
            lon = adressInfos.get(0).lon();
        }
        else {
            lat = 0.0;
            lon = 0.0;
        }
        bp.setLat(lat);
        bp.setLon(lon);
        BenutzerProfil bpnew = benutzerprofilrepo.save(bp);
        logger.info("profil = {}", bpnew);
        return bpnew;
    }

    @Override
    public Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id) {
        Optional<BenutzerProfil> bp = benutzerprofilrepo.findById(id);
        if (bp.isEmpty()) {
            bp = Optional.empty();
        }
        return bp;
    }

    @Override
    public List<BenutzerProfil> alleBenutzerProfile() {
        List<BenutzerProfil> bplist = benutzerprofilrepo.findAll(Sort.by("name"));
        return bplist;
    }

    @Override
    public void loescheBenutzerProfilMitId(Long loesch) {
        benutzerprofilrepo.deleteById(loesch);
    }

    @Override @Transactional
    public void fuegeAngebotHinzu(Long id, Angebot angebot) {
        double lat, lon;
        List<AdressInfo> adressInfos = geoservice.findeAdressInfo(angebot.getAbholort());
        if (!adressInfos.isEmpty()) { 
            lat = adressInfos.get(0).lat();
            lon = adressInfos.get(0).lon();
        }
        else {
            lat = 0.0;
            lon = 0.0;
        }
        angebot.setLat(lat);
        angebot.setLon(lon);
        angebotsrepo.save(angebot);

        Optional<BenutzerProfil> bp = holeBenutzerProfilMitId(id);
        BenutzerProfil  profil = bp.get();
        List<Angebot> angeboteAktu = profil.getAngebote();
        angeboteAktu.add(angebot);
        profil.setAngebote(angeboteAktu);
        angebot.setAnbieter(profil);
        benutzerprofilrepo.save(profil);
    }

    @Override @Transactional
    public void loescheAngebot(Long id) {
        Angebot angebot = angebotsrepo.getById(id);
        BenutzerProfil profil = angebot.getAnbieter();
        List<Angebot> angeboteAktu = profil.getAngebote();
        angeboteAktu.remove(angebot);
        profil.setAngebote(angeboteAktu);
        angebotsrepo.deleteById(id);
    }

    @Override
    public List<Angebot> alleAngebote() {
        List<Angebot> alleAngebote = angebotsrepo.findAll();
        return alleAngebote;
    }

    @Override
    public Optional<Angebot> findeAngebotMitID(long angebotid) {
        Optional<Angebot> angebotMitID = angebotsrepo.findById(angebotid);
        if (angebotMitID.isEmpty()) {
            angebotMitID = Optional.empty();
        }
        return angebotMitID;
    }

}