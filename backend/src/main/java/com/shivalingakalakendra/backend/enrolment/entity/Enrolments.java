package com.shivalingakalakendra.backend.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "enrolments")
public class Enrolments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="email", nullable = false, length = 320)
    private String email;

    @Column(name="guardianemail", length = 320)
    private String guardianEmail;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 24)
    private String phone;

    @Column(name = "status", nullable = false, length = 40)
    private String status;

    @Column(name = "dateofbirth", nullable = false, length = 10)
    private String dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "courseid",
            foreignKey = @ForeignKey(name = "fk_courses_enrolments_courses"))
    private Courses courseid;

    @ManyToOne
    @JoinColumn(name = "batchid",
            foreignKey = @ForeignKey(name = "fk_batches_enrolments_batches"))
    private Batches batchid;

    @Column(name = "startdate", nullable = true, length = 100)
    private Timestamp startDate;

    @Column(name = "createTimeStamp", nullable = false, length = 100)
    private Timestamp createTimeStamp;
}
