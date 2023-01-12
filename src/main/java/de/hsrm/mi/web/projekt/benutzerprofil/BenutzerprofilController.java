package de.hsrm.mi.web.projekt.benutzerprofil;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.messaging.BackendInfoServiceImpl;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;
import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserServiceImpl;

@Controller
@SessionAttributes(names = {"profil", "profilliste"})
@RequestMapping("/benutzerprofil")
public class BenutzerprofilController {
    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);
    @Autowired private BenutzerprofilServiceImpl benutzerprofilservice;
    @Autowired private BackendInfoServiceImpl backendinfoservice;
    @Autowired private ProjektUserServiceImpl projektuserservice;

    @ModelAttribute("profil")
    public void initProfil(Model m, Principal prinz) {
        if (prinz == null) {
            m.addAttribute("profil", new BenutzerProfil());
        } else {
            String loginname = prinz.getName();
            logger.warn("loginname = {}", loginname);
            ProjektUser user = projektuserservice.findeBenutzer(loginname);
            BenutzerProfil profil = user.getBenutzerProfil();
            m.addAttribute("profil", profil);
            logger.warn("profil = {}", profil);
        }
    }
    @ModelAttribute("profilliste")
    public void initProfilliste(Model m){
        m.addAttribute("profilliste", new ArrayList<BenutzerProfil>());
    }

    @GetMapping()
    public String benutzerprofil_get(Model m){
        return "benutzerprofil/profilansicht";
    }

    @GetMapping("/bearbeiten")
    public String profileditor_get(Model m){
        return "benutzerprofil/profileditor";
    }

    @PostMapping("/bearbeiten")
    public String profileditor_post(Model m, @Valid @ModelAttribute("profil") BenutzerProfil profil, BindingResult result){
        logger.info("profil = {}", profil);
        logger.info(result.getAllErrors().toString());
        m.addAttribute("profil", profil);
        if (result.hasErrors()){
            return "benutzerprofil/profileditor";
        }
        BenutzerProfil newProfil = benutzerprofilservice.speichereBenutzerProfil(profil);
        logger.info("profil = {}", newProfil);
        m.addAttribute("profil", newProfil);
        return "redirect:/benutzerprofil";
    }

    @GetMapping("/clearsession")
    public String clearsession_get(Model m, SessionStatus status){
        status.setComplete();
        return "redirect:/benutzerprofil";
    }

    @GetMapping("/liste")
    public String liste_get(Model m, @ModelAttribute("profilliste") List<BenutzerProfil> profilliste, @ModelAttribute("profil") BenutzerProfil profil, @RequestParam(required = false) String op, @RequestParam(required = false) Long id){
        if (op != null){
            if (op.equals("loeschen")){
                benutzerprofilservice.loescheBenutzerProfilMitId(id);
                return "redirect:/benutzerprofil/liste";
            }
            else if (op.equals("bearbeiten")) {
                Optional<BenutzerProfil> bp = benutzerprofilservice.holeBenutzerProfilMitId(id);
                if (bp.isPresent()){
                    profil = bp.get();
                    m.addAttribute("profil", profil);
                }
                return "redirect:/benutzerprofil/bearbeiten";
            }
        }
        profilliste = benutzerprofilservice.alleBenutzerProfile();
        m.addAttribute("profilliste", profilliste);
        logger.info("profilliste = {}", profilliste);
        return "benutzerprofil/profilliste";
    }

    @GetMapping("/angebot")
    public String angebot_get(Model m) {
        m.addAttribute("angebot", new Angebot());
        return "benutzerprofil/angebotsformular";
    }

    @PostMapping("/angebot")
    public String angebot_post(Model m, @ModelAttribute("profil") BenutzerProfil profil, @Valid @ModelAttribute("angebot") Angebot angebot) {
        //m.addAttribute("angebot", angebot);

        //BenutzerProfil profil = (BenutzerProfil)m.getAttribute("profil");
        benutzerprofilservice.fuegeAngebotHinzu(profil.getId(), angebot);

        Optional<BenutzerProfil> bp = benutzerprofilservice.holeBenutzerProfilMitId(profil.getId());
        BenutzerProfil newProfil = bp.get();
        //BenutzerProfil newnewProfil = benutzerprofilservice.speichereBenutzerProfil(newProfil);

        m.addAttribute("profil", newProfil);

        backendinfoservice.sendInfo("angebot", BackendOperation.CREATE, angebot.getId());

        return "redirect:/benutzerprofil";
    }

    @GetMapping("/angebot/{id}/del")
    public String angebotdel_get(Model m, @ModelAttribute("profil") BenutzerProfil profil, @PathVariable("id") Long id){
        benutzerprofilservice.loescheAngebot(id);

        //BenutzerProfil pofil = (BenutzerProfil)m.getAttribute("profil");
        Optional<BenutzerProfil> bp = benutzerprofilservice.holeBenutzerProfilMitId(profil.getId());
        BenutzerProfil newProfil = bp.get();
        //BenutzerProfil newnewProfil = benutzerprofilservice.speichereBenutzerProfil(newProfil);
        
        m.addAttribute("profil", newProfil);

        backendinfoservice.sendInfo("angebot", BackendOperation.DELETE, id);

        return "redirect:/benutzerprofil";
    }
    
}