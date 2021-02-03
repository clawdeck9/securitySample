package com.cluster9.securitySample.persistence;

import com.cluster9.securitySample.Entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    public AppRole findByAppRoleName(String role);
}
