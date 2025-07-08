package com.pieceofcake.alert_service.alert.application;

import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import com.pieceofcake.alert_service.kafka.event.AlertKafkaEvent;
import reactor.core.publisher.Flux;

public interface AlertService {
    void getAlert(AlertKafkaEvent alertKafkaEvent, AlertType alertType);
    Flux<AlertResponseDto> getAlertByMemberUuid(String memberUuid);
}
