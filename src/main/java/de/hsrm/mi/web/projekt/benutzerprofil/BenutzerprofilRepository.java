package de.hsrm.mi.web.projekt.benutzerprofil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenutzerprofilRepository extends JpaRepository<BenutzerProfil, Long> {
    
}