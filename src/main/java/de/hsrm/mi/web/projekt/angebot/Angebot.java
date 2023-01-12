package de.hsrm.mi.web.projekt.angebot;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.gebot.Gebot;

@Entity
public class Angebot implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @Version
    private long version;
    @NotNull
    private String beschreibung;
    private long mindestpreis;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @FutureOrPresent @NotNull
    private LocalDateTime ablaufzeitpunkt;
    private String abholort;
    private double lat;
    private double lon;
    @ManyToOne
    private BenutzerProfil anbieter;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "angebot", fetch = FetchType.EAGER)
    private List<Gebot> gebote;

    public Angebot() {
        this.beschreibung = "";
        this.mindestpreis = 0;
        this.ablaufzeitpunkt = LocalDateTime.now();
        this.abholort = "";
        this.lat = 0.0;
        this.lon = 0.0;
        this.gebote = new ArrayList<Gebot>();
    }


    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public long getMindestpreis() {
        return mindestpreis;
    }

    public void setMindestpreis(long mindestpreis) {
        this.mindestpreis = mindestpreis;
    }

    public LocalDateTime getAblaufzeitpunkt() {
        return ablaufzeitpunkt;
    }

    public void setAblaufzeitpunkt(LocalDateTime ablaufzeitpunkt) {
        this.ablaufzeitpunkt = ablaufzeitpunkt;
    }

    public String getAbholort() {
        return abholort;
    }

    public void setAbholort(String abholort) {
        this.abholort = abholort;
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

    public BenutzerProfil getAnbieter() {
        return anbieter;
    }

    public void setAnbieter(BenutzerProfil anbieter) {
        this.anbieter = anbieter;
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
        
}
