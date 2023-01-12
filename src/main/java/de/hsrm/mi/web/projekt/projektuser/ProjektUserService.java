package de.hsrm.mi.web.projekt.projektuser;

public interface ProjektUserService {
    public ProjektUser neuenBenutzerAnlegen(String username, String klartextpasswort, String rolle);
    public ProjektUser findeBenutzer(String username);
}
