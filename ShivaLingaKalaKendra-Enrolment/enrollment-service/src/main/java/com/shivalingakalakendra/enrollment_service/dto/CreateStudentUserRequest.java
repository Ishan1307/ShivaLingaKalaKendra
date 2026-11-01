package com.shivalingakalakendra.enrollment_service.dto;

public record CreateStudentUserRequest(

    String firstName,
    String lastName,
    String email
) {

    // Constructors
//    public CreateStudentUserRequest() {
//    }
//
//    public CreateStudentUserRequest(String firstName, String lastName, String email) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//    }

}
