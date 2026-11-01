package com.shivalingakalakendra.enrollment_service.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "enrolments")
public class Enrolments {


    private String id;


    private String email;


    private String guardianEmail;


    private String firstName;


    private String lastName;


    private String phone;


    private String status;


    private String dateOfBirth;


    private String courseid;


    private String batchid;

    private String userid;


    private LocalDate startDate;


    private LocalDateTime createTimeStamp;
}
