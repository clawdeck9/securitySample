package com.cluster9.securitySample.security;

import com.cluster9.securitySample.entities.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

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
            // throw new AuthenticationException();
        }

        AppUser appUser = null;
        try {
            appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return am.authenticate(new UsernamePasswordAuthenticationToken(appUser.getName(), appUser.getPassword()));

    }

    // create and set up a jwt from an Authentication instance
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String jwt = Jwts.builder().setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+SecurityConst.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SecurityConst.SECRET)
                .claim("roles", user.getAuthorities())
                .compact();
        response.addHeader("authorization", SecurityConst.TOKEN_PREFIX + jwt);
    }
}
