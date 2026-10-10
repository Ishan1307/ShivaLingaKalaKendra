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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "isActive", nullable = false, length = 100)
    private Boolean isActive;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;

    @Column(name = "modifyTimeStamp", nullable = false, length = 100)
    private Timestamp modifyTimeStamp;

    protected User() {
    }

    public User(
            String email,
            String firstName,
            String lastName,
            String password,
            Boolean isActive,
            Timestamp createTimeStamp,
            Timestamp modifyTimeStamp
    ) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isActive = isActive;
        this.createTimeStamp = createTimeStamp;
        this.modifyTimeStamp = modifyTimeStamp;
    }

}