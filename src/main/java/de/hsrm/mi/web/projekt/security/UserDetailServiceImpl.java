package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserRepository;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserServiceException;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired private ProjektUserRepository projektuserrepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ProjektUser user = projektuserrepo.findById(username)
            .orElseThrow(() -> new ProjektUserServiceException(username));
        
        return org.springframework.security.core.userdetails.User
            .withUsername(username)
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
    
}
