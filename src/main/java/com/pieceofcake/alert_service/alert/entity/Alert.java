package com.pieceofcake.alert_service.alert.entity;

import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@ToString
@Getter
@NoArgsConstructor
@Document(collection = "alert_message_entity")
public class Alert {
    @Id
    private String id;
    private AlertType alertType;
    private String key;
    private String message;
    private String memberUuid;
    private Boolean commonAlert = true;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    @Builder
    public Alert(
            AlertType alertType,
            String key,
            String message,
            String memberUuid,
            Boolean commonAlert
    ) {
        this.alertType = alertType;
        this.key = key;
        this.message = message;
        this.memberUuid = memberUuid;
        this.commonAlert = commonAlert;
    }
}
