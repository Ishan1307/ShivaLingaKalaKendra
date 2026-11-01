package com.shivalingakalakendra.identity_service.repository;

import com.shivalingakalakendra.identity_service.entity.UserRoles;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRolesRepository extends JpaRepository<UserRoles, UUID> {

    @EntityGraph(attributePaths = "role")
    List<UserRoles> findAllByUser_Id(UUID userId);
}
