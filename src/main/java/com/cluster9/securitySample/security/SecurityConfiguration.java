package com.cluster9.securitySample.security;

import com.cluster9.securitySample.services.AccountService;
import com.cluster9.securitySample.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


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
                }).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // by default the auth is stateful, with sessionid and csrf, so here it's a stateful impl
        http
                .csrf().disable();
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        http.formLogin()
        http
                .authorizeRequests().antMatchers("/h2-console/**").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated();
        http
                .headers().frameOptions().disable();
        http
                .addFilter(new AppAuthenFilter(authenticationManager()));
        http
//                .addFilterBefore(new AuthorizFilter(), UsernamePasswordAuthenticationFilter.class);
                .addFilterBefore(new AuthorizFilter(accountService), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
