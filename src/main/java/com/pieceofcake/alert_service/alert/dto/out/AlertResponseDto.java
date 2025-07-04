package com.pieceofcake.alert_service.alert.dto.out;

import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlertResponseDto {
    private String key;
    private String message;
    private AlertType alertType;
    private String memberUuid;
    private Boolean commonAlert;


    @Builder
    public AlertResponseDto(
            String key,
            String message,
            AlertType alertType,
            String memberUuid,
            Boolean commonAlert
    ) {
        this.key = key;
        this.message = message;
        this.alertType = alertType;
        this.memberUuid = memberUuid;
        this.commonAlert = commonAlert;
    }

    public static AlertResponseDto of(
            String key,
            String message,
            AlertType alertType,
            String memberUuid,
            Boolean commonAlert
    ) {
        return AlertResponseDto.builder()
                .key(key)
                .message(message)
                .alertType(alertType)
                .memberUuid(memberUuid)
                .commonAlert(commonAlert)
                .build();
    }

    public AlertResponseVo toVo() {
        return AlertResponseVo.builder()
                .key(key)
                .message(message)
                .alertType(alertType)
                .build();
    }
}
