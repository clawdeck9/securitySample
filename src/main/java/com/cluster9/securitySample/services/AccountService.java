package com.cluster9.securitySample.services;

import com.cluster9.securitySample.Entities.AppRole;
import com.cluster9.securitySample.Entities.AppUser;

import java.util.Collection;

public interface AccountService {
    AppRole addAppRole(String name);
    AppUser addAppUser(String name, String password);
    void addRoleToUser(AppUser user, AppRole role);
    AppUser findUserByName(String name);
    Collection<AppUser> findAll();
}
