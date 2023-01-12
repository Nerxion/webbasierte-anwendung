package de.hsrm.mi.web.projekt.benutzerprofil;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.validierung.Bunt;

@Entity
public class BenutzerProfil implements Serializable{
    @Id
    @GeneratedValue
    private long id;
    @Version
    private long version;
    @Size(min=3,max=60) @NotNull
    private String name;
    @DateTimeFormat(iso = ISO.DATE)
    @PastOrPresent @NotNull
    private LocalDate geburtsdatum;
    @NotNull
    private String adresse;
    @Email
    private String email;
    @Bunt(message="{profil.buntfehler}")
    private String lieblingsfarbe;
    @NotNull
    private String interessen;
    private double lat;
    private double lon;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "anbieter", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Angebot> angebote;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gebieter", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Gebot> gebote;
    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "benutzerProfil")
    private ProjektUser projektuser;

    public BenutzerProfil(){
        this.name = "";
        this.geburtsdatum = LocalDate.of(1,1,1);
        this.adresse = "";
        this.email = null;
        this.lieblingsfarbe = "";
        this.interessen = "";
        this.angebote = new ArrayList<Angebot>();
        this.gebote = new ArrayList<Gebot>();
    }

    public BenutzerProfil(String name, LocalDate geburtsdatum, String adresse, String email, String lieblingsfarbe, String interessen, double lat, double lon){
        this.name = name;
        this.geburtsdatum = geburtsdatum;
        this.adresse = adresse;
        this.email = email;
        this.lieblingsfarbe = lieblingsfarbe;
        this.interessen = interessen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLieblingsfarbe() {
        return lieblingsfarbe;
    }

    public void setLieblingsfarbe(String lieblingsfarbe) {
        this.lieblingsfarbe = lieblingsfarbe;
    }

    public String getInteressen() {
        return interessen;
    }

    public void setInteressen(String interessen) {
        this.interessen = interessen;
    }

    public List<String> getInteressenListe() {
        List<String> interessenListe;
        if (interessen.isEmpty()){
            interessenListe = new ArrayList<String>();
        } else {
            interessenListe = new ArrayList<String>(Arrays.asList(interessen.split(",")));
            for (int i = 0; i < interessenListe.size(); i++) {
                String ele = interessenListe.get(i);
                ele = ele.trim();
                interessenListe.set(i, ele);
            }
        }
        return interessenListe;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<Angebot> getAngebote() {
        return angebote;
    }

    public void setAngebote(List<Angebot> angebote) {
        this.angebote = angebote;
    }

    public List<Gebot> getGebote() {
        return gebote;
    }

    public void addGebot(Gebot gebot) {
        this.gebote.add(gebot);
    }

    public void removeGebot(Gebot gebot) {
        this.gebote.remove(gebot);
    }

    public ProjektUser getProjektuser() {
        return projektuser;
    }

    public void setProjektuser(ProjektUser projektuser) {
        this.projektuser = projektuser;
    }

    @Override
    public String toString() {
        return "BenutzerProfil [adresse=" + adresse + ", email=" + email + ", geburtsdatum=" + geburtsdatum + ", id="
                + id + ", interessen=" + interessen + ", lat=" + lat + ", lieblingsfarbe=" + lieblingsfarbe + ", lon="
                + lon + ", name=" + name + ", version=" + version + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geburtsdatum == null) ? 0 : geburtsdatum.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        BenutzerProfil other = (BenutzerProfil) obj;
        if (geburtsdatum == null) {
            if (other.geburtsdatum != null)
                return false;
        } else if (!geburtsdatum.equals(other.geburtsdatum))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
