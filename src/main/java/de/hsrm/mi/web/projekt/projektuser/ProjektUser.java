package de.hsrm.mi.web.projekt.projektuser;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

@Entity
public class ProjektUser implements Serializable {
    @Id @NotBlank @Size(min=3)
    private String username;
    @NotBlank @Size(min=3)
    private String password;
    private String role = "";
    @OneToOne
    private BenutzerProfil benutzerProfil;

    public ProjektUser() {
        this.username = "platzhalter";
        this.password = "platzhalter";
        this.role = "";
    }

    public ProjektUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BenutzerProfil getBenutzerProfil() {
        return benutzerProfil;
    }

    public void setBenutzerProfil(BenutzerProfil benutzerProfil) {
        this.benutzerProfil = benutzerProfil;
    }

    @Override
    public String toString() {
        return "ProjektUser [password=" + password + ", role=" + role + ", username=" + username + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProjektUser other = (ProjektUser) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    
    
}
