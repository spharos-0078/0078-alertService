package com.pieceofcake.alert_service.kafka.event;

import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class AlertKafkaEvent {
    private String Key;
    private String message;
    private AlertType alertType;
    private String memberUuid;
    private Boolean commonAlert = true;

    @Builder
    public AlertKafkaEvent(
            String key,
            String message,
            AlertType alertType,
            String memberUuid,
            boolean commonAlert) {
        this.Key = key;
        this.message = message;
        this.alertType = alertType;
        this.memberUuid = memberUuid;
        this.commonAlert = commonAlert;
    }
}
