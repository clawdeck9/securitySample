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
            //logger.debug("authorization header = "+authHeader);
            String username = authHeader.substring(7);
            AppUser appUser = accountService.findUserByName(username);
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            appUser.getRoles().forEach( role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(appUser.getName(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
            System.out.println("context:"+SecurityContextHolder.getContext().toString());
        } else {
            logger.debug("the authorization filter do not know this username ");
        }
        filterChain.doFilter(request, response);
    }
}
