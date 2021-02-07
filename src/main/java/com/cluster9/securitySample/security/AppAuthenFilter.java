package com.cluster9.securitySample.security;

import com.cluster9.securitySample.entities.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class AppAuthenFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(AppAuthenFilter.class);
    AuthenticationManager am;

    public AppAuthenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.am = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        logger.debug("authentication filter starts");
        String authHeader = request.getHeader("authorization");
        if(authHeader!=null){
            logger.debug("there should not be a jwt in the authenticationFilter: "+ authHeader);
        }

        AppUser appUser = null;
        try {
            appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return am.authenticate(new UsernamePasswordAuthenticationToken(appUser.getName(), appUser.getPassword()));

    }

    // create and set up a jwt from authentication instance
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // temp
        authResult.getPrincipal();
        authResult.getAuthorities();
        System.out.println(authResult.getAuthorities());
        response.addHeader("authorisation", "bearer " + authResult.getName());
    }
}
