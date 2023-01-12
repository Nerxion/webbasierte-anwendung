package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration @EnableWebSecurity
public class ProjektSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired UserDetailServiceImpl userdetailservice;

    @Bean PasswordEncoder passwordEncoder() { // @Bean -> Encoder woanders per @Autowired abrufbar
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {
        PasswordEncoder pwenc = passwordEncoder();

        authmanagerbuilder.inMemoryAuthentication() // "in memory"-Benutzerdatenbank anlegen
            .withUser("friedfert")
            .password(pwenc.encode("dingdong")) // Passwörter nicht als Klartext speichern, daher encoded :D -> unumkehrbar
            .roles("USER")
        .and()
            .withUser("joghurta")
            .password(pwenc.encode("chefin"))
            .roles("ADMIN");

        authmanagerbuilder
            .userDetailsService(userdetailservice)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/stompbroker/**").permitAll()
            .antMatchers("/registrieren").permitAll() // TO DO: css access noch machen
            .antMatchers(HttpMethod.POST).hasAnyRole("USER", "ADMIN")
            .antMatchers(HttpMethod.GET).hasAnyRole("USER", "ADMIN")
        .and()
            .formLogin()
            .defaultSuccessUrl("/benutzerprofil")
            .permitAll()
        .and()
            .logout()
            .permitAll()
        .and()
            .csrf().ignoringAntMatchers("/h2-console/**")
        .and()
            .headers().frameOptions().disable();
    }
}
