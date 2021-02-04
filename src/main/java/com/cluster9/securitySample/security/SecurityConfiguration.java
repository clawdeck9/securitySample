package com.cluster9.securitySample.security;

import com.cluster9.securitySample.services.AccountService;
import com.cluster9.securitySample.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    AccountService accountService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                        accountService.findUserByName(username).getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
                        return new User(username,  accountService.findUserByName(username).getPassword(), authorities);
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // by default the auth is stateful, with sessionid and csrf, so here it's a stateful impl
        http
                .csrf().disable();
        http
                .formLogin()
                .and()
                .authorizeRequests().anyRequest().authenticated();
        http
                .headers().frameOptions().disable();

    }
}
