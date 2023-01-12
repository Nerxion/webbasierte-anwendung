package de.hsrm.mi.web.projekt.projektuser;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilServiceImpl;

@Service
public class ProjektUserServiceImpl implements ProjektUserService {
    Logger logger = LoggerFactory.getLogger(ProjektUserServiceImpl.class);
    @Autowired private PasswordEncoder pwenc;
    @Autowired private ProjektUserRepository projektuserrepo;
    @Autowired private BenutzerprofilServiceImpl benutzerprofilservice;

    @Override
    public ProjektUser neuenBenutzerAnlegen(String username, String klartextpasswort, String rolle) {
        String encpassword = pwenc.encode(klartextpasswort);
        if (rolle == null || rolle == "") {
            rolle = "USER";
        }
        Optional<ProjektUser> optuser = projektuserrepo.findById(username);
        if (!optuser.isEmpty()) {
            throw new ProjektUserServiceException("User existiert schon");
        }
        ProjektUser user = new ProjektUser(username, encpassword, rolle);

        BenutzerProfil profil = new BenutzerProfil();
        profil.setName(username);

        BenutzerProfil profilsaved = benutzerprofilservice.speichereBenutzerProfil(profil);
        logger.warn("NewProfil = {}", profilsaved);

        user.setBenutzerProfil(profilsaved);

        ProjektUser usersaved = projektuserrepo.save(user);
        logger.warn("ProjektUser = {}", usersaved);

        profilsaved.setProjektuser(usersaved);


        return usersaved;
    }

    @Override
    public ProjektUser findeBenutzer(String username) {
        Optional<ProjektUser> optuser = projektuserrepo.findById(username);
        if (optuser.isEmpty()) {
            throw new ProjektUserServiceException("User existiert nicht");
        }
        ProjektUser user = optuser.get();
        return user;
    }
    
}
