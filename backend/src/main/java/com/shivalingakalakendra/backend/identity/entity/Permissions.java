package com.shivalingakalakendra.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permissions {

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

    protected Permissions() {
    }

    public Permissions(
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
