package de.hsrm.mi.web.projekt.projektuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjektUserRepository extends JpaRepository<ProjektUser, String> {
    
}
