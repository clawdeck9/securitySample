package com.cluster9.securitySample.services;

import com.cluster9.securitySample.Entities.AppRole;
import com.cluster9.securitySample.Entities.AppUser;
import com.cluster9.securitySample.persistence.AppRoleRepository;
import com.cluster9.securitySample.persistence.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Service
@Transactional  // @transactional from Spring!!!
public class AccountServiceImpl implements AccountService {

    AppUserRepository appUserRepository;
    AppRoleRepository appRoleRepository;

    @Override
    public AppRole addAppRole(String name){
        return appRoleRepository.save(new AppRole(name));
    }

    @Override
    public AppUser addAppUser(String name, String password) {
        return appUserRepository.save(new AppUser(name, password));
    }

    @Override
    public void addRoleToUser(String user, String role) {
        appUserRepository.findByName(user).getRoles().add(appRoleRepository.findByName(role));
    }

    @Override
    public AppUser findUserByName(String name) {
        return appUserRepository.findByName(name);
    }

    @Override
    public Collection<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;

    }
}
