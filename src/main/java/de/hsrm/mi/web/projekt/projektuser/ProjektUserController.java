package de.hsrm.mi.web.projekt.projektuser;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProjektUserController {
    Logger logger = LoggerFactory.getLogger(ProjektUserController.class);
    @Autowired private ProjektUserServiceImpl projektuserservice;
    
    @GetMapping("/registrieren")
    public String registrieren_get(Model m) {
        m.addAttribute("projektuser", new ProjektUser());
        return "projektuser/registrieren";
    }

    @PostMapping("/registrieren")
    public String registrieren_post(Model m, @Valid @ModelAttribute("projektuser") ProjektUser projektuser, BindingResult result) {
        logger.warn("USER = {}", projektuser);
        m.addAttribute("projektuser", projektuser);
        if (result.hasErrors()) {
            return "projektuser/registrieren";
        }
        try {
            ProjektUser newuser = projektuserservice.neuenBenutzerAnlegen(projektuser.getUsername(), projektuser.getPassword(), projektuser.getRole());
            logger.warn("NEWUSER = {}", newuser);
            m.addAttribute("projektuser", newuser);
        } catch (ProjektUserServiceException e) {
            e.printStackTrace();
            return "projektuser/registrieren";
        }
        return "redirect:/benutzerprofil";
    }
}
