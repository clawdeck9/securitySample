package com.cluster9.securitySample.services;

import com.cluster9.securitySample.Entities.AppRole;
import com.cluster9.securitySample.Entities.AppUser;

import java.util.Collection;

public interface AccountService {
    AppRole addAppRole(String name);
    AppUser addAppUser(String name, String password);
    void addRoleToUser(String userName, String roleName);
    AppUser findUserByName(String name);
    Collection<AppUser> findAll();
}
