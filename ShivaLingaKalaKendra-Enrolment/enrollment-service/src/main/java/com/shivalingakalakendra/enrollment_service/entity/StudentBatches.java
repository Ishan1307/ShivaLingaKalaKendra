package com.shivalingakalakendra.enrollment_service.entity;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "student_batches")
public class StudentBatches {

    private String id;

    private String userid;

    private String batchid;

    private String enrollmentId;

    private Timestamp createTimeStamp;
    public  StudentBatches() {
    }

    public StudentBatches(
            String userid,
            String batchid,
            String enrollmentId,
            Timestamp createTimeStamp
    ) {
        this.userid = userid;
        this.batchid = batchid;
        this.enrollmentId = enrollmentId;
        this.createTimeStamp = createTimeStamp;
    }
}
