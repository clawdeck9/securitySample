package com.cluster9.securitySample.persistence;

import com.cluster9.securitySample.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public AppUser findByName(String user);
}
