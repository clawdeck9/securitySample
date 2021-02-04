package com.cluster9.securitySample.persistence;

import com.cluster9.securitySample.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    public AppRole findByName(String role);
}
