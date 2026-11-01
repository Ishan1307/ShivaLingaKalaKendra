package com.shivalingakalakendra.enrollment_service.external;

import com.shivalingakalakendra.enrollment_service.dto.CreateStudentUserRequest;
import com.shivalingakalakendra.enrollment_service.dto.CreateStudentUserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class IdentityServiceClient {

    private final RestClient restClient;

    public IdentityServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public CreateStudentUserResponse createStudent(
            CreateStudentUserRequest request) {

        return restClient.post()
                .uri("http://localhost:8081/api/users/createuser/student")
                .headers(headers -> headers.setBasicAuth(
                        "ishan",
                        "abc123"
                ))
                .body(request)
                .retrieve()
                .body(CreateStudentUserResponse.class);
    }
}