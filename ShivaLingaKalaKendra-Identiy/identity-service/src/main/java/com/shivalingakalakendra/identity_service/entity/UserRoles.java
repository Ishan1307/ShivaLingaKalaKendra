package com.shivalingakalakendra.identity_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "userRoles",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_roles_user_role",
                columnNames = {"userid", "roleid"}))
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userid", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_roles_user"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roleid", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_roles_role"))
    private Roles role;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;

    public UserRoles() {
    }

    public UserRoles(
            User user,
            Roles role,
            Timestamp createTimeStamp
    ) {
        this.user = user;
        this.role = role;
        this.createTimeStamp = createTimeStamp;
    }
}
