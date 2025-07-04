package com.pieceofcake.alert_service.alert.application;

import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
import com.pieceofcake.alert_service.kafka.event.AlertKafkaEvent;
import reactor.core.publisher.Flux;

public interface AlertService {
    AlertResponseDto getAlert(AlertKafkaEvent alertKafkaEvent);

    // SSE 연결을 위한 메소드 추가
    Flux<AlertResponseVo> getAlertStream();

    // 새로 추가할 메소드
    Flux<AlertResponseVo> getAlertStreamForMember(String memberUuid);
    Flux<AlertResponseVo> getCommonAlertStream();
}
