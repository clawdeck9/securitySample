package com.cluster9.securitySample.controlers;

import com.cluster9.securitySample.entities.AppRole;
import com.cluster9.securitySample.entities.AppUser;
import com.cluster9.securitySample.services.AccountServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersControler {
    AccountServiceImpl accountService;

    @PostMapping(path = "users")
    AppUser addAppUser(@RequestBody BasicUser user){
        return accountService.addAppUser(user.getName(), user.getPassword());
    }
    @PostMapping(path = "roles")
    AppRole addAppRole(@RequestBody AppRole role){
        return accountService.addAppRole(role.getName());
    }

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


