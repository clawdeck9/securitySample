package com.cluster9.securitySample.persistence;

import com.cluster9.securitySample.Entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public AppUser findByAppUserName(String user);
}
