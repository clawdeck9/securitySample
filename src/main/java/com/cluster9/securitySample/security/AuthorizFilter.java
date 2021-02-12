package com.cluster9.securitySample.security;

import com.cluster9.securitySample.entities.AppUser;
import com.cluster9.securitySample.services.AccountService;
import com.cluster9.securitySample.services.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthorizFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizFilter.class);
//    @Autowired
    AccountServiceImpl accountService;

    public AuthorizFilter() {
    }
    public AuthorizFilter(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // TODO: change this to a jwt token
        logger.debug("authorization filter starts");
        //if authorization bearer is ok, add all the credentials to the auth manager.
        String authHeader = request.getHeader("authorization");
        if(authHeader != null){;
            logger.debug("authorization header = "+authHeader);
            // if the jwt does not start with the bearer constant, stop here
            if(! authHeader.startsWith(SecurityConst.TOKEN_PREFIX)){
                filterChain.doFilter(request, response);
                return;
            }
            // decode jwt and create a auth token for further methods access control:
            String trialJwt = authHeader.substring(7);
            // the Jwts.parser() checks the jwt validity? at least, it requires the SECRET string
            Claims claims = Jwts.parser()
                    .setSigningKey(SecurityConst.SECRET)
                    //.parseClaimsJws(trialJwt.replace(SecurityConst.TOKEN_PREFIX, ""))// suppress the prefix in the token string
                    .parseClaimsJws(trialJwt)
                    .getBody();
            //logger.debug("claims from the authorizationFilter = " + claims);
            String username = claims.getSubject();
            ArrayList<Map<String, String>> roles = (ArrayList<Map<String,String>>) claims.get("roles");
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.get("authority"))));
            // if the user is not validated, the filter is called in a 'anonymous user' context and the req will be rejected for all authentication-required methods

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
            logger.debug("context:"+SecurityContextHolder.getContext().toString());
        } else {
            logger.debug("there is no authorization header");
        }
        filterChain.doFilter(request, response);
        return;
    }
}
