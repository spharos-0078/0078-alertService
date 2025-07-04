package com.pieceofcake.alert_service.alert.vo.out;

import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlertResponseVo {

    private String key;
    private String message;
    private AlertType alertType;

    @Builder
    public AlertResponseVo(
            String key,
            String message,
            AlertType alertType
    ) {
        this.key = key;
        this.message = message;
        this.alertType = alertType;
    }
}
