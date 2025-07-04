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
    private String key;
    private String message;
    private String memberUuid;
    private Boolean commonAlert = true;

    @Builder
    public AlertKafkaEvent(
            String key,
            String message,
            String memberUuid,
            boolean commonAlert) {
        this.key = key;
        this.message = message;
        this.memberUuid = memberUuid;
        this.commonAlert = commonAlert;
    }
}
