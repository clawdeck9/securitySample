package com.cluster9.securitySample.controlers;

import com.cluster9.securitySample.entities.AppRole;
import com.cluster9.securitySample.entities.AppUser;
import com.cluster9.securitySample.security.AuthorizFilter;
import com.cluster9.securitySample.services.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersControler {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizFilter.class);
    AccountServiceImpl accountService;

//    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "users")
    @PreAuthorize("hasAuthority('USER')")
    AppUser addAppUser(@RequestBody BasicUser user){
        logger.debug("in controller: added user is: "+user.toString());
        return accountService.addAppUser(user.getName(), user.getPassword());
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "roles")
    AppRole addAppRole(@RequestBody AppRole role){
        return accountService.addAppRole(role.getName());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "roleUser")
    void addRoleToUser(@RequestBody  RoleToUser rtu){
        accountService.addRoleToUser(rtu.getUser(), rtu.getRole());
    }

    public UsersControler(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    static class BasicUser{
        private String name;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String toString(){
            return name;
        }
    }
    static class RoleToUser{
        private String user;
        private String role;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}


