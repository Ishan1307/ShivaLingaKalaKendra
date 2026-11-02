package com.shivalingakalakendra.backend.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rolePermissions"
    , uniqueConstraints = @UniqueConstraint(
        name = "uk_role_permissions_role_permission",
        columnNames = {"roleid", "permissionid"}))
public class RolePermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roleid", nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_permissions_role"))
    private Roles role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permissionid", nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_permissions_permission"))
    private Permissions permission;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;

    protected RolePermissions() {
    }

    public RolePermissions(
            Roles role,
            Permissions permission,
            Timestamp createTimeStamp
    ) {
        this.role = role;
        this.permission = permission;
        this.createTimeStamp = createTimeStamp;
    }
}
