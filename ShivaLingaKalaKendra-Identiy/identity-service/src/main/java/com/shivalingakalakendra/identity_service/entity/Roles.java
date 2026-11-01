package com.shivalingakalakendra.identity_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = false, length = 100)
    private String description;

    @Column(nullable = false, unique = false, length = 100)
    private String createdBy;

    @Column(nullable = false, unique = false, length = 100)
    private String modifiedBy;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;

    @Column(name = "modifyTimeStamp", nullable = false, length = 100)
    private Timestamp modifyTimeStamp;

    protected Roles() {
    }

    public Roles(
            String name,
            String description,
            String createdBy,
            String modifiedBy,
            Timestamp createTimeStamp,
            Timestamp modifyTimeStamp
    ) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createTimeStamp = createTimeStamp;
        this.modifyTimeStamp = modifyTimeStamp;
    }

}
