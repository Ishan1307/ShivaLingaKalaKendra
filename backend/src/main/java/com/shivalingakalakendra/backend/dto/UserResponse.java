package com.shivalingakalakendra.backend.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        Boolean isActive,
        Timestamp createTimeStamp,
        Timestamp modifyTimeStamp
) {
}
