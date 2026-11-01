package com.shivalingakalakendra.identity_service.repository;

import com.shivalingakalakendra.identity_service.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Roles, UUID> {

    public Roles findByName(String name);

}
