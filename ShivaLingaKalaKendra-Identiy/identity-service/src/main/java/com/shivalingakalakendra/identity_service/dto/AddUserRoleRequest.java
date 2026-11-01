package com.shivalingakalakendra.identity_service.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record AddUserRoleRequest(
    UUID userId,
    UUID roleId,
    Timestamp createTimeStamp
) {
}
