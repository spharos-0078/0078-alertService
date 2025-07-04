package com.pieceofcake.alert_service.alert.application;

import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
import com.pieceofcake.alert_service.kafka.event.AlertKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService{

    private final Sinks.Many<AlertResponseDto> alertSink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public AlertResponseDto getAlert(AlertKafkaEvent alertKafkaEvent, AlertType alertType) {
        AlertResponseDto responseDto = AlertResponseDto.of(
                alertKafkaEvent.getKey(),
                alertKafkaEvent.getMessage(),
                alertType,
                alertKafkaEvent.getMemberUuid(),
                alertKafkaEvent.getCommonAlert()
        );

        // 알람 발생 시 Sink에 이벤트 발행
        alertSink.tryEmitNext(responseDto);

        return responseDto;
    }

    @Override
    public Flux<AlertResponseVo> getAlertStream() {
        // Sink로부터 Flux를 얻어 AlertResponseVo로 변환
        return alertSink.asFlux().map(AlertResponseDto::toVo);
    }

    @Override
    public Flux<AlertResponseVo> getMemberAlertStream(String memberUuid) {
        // 특정 회원의 알림과 공통 알림 모두 제공
        return alertSink.asFlux()
                .filter(dto -> dto.getCommonAlert() ||
                        (dto.getMemberUuid() != null && dto.getMemberUuid().equals(memberUuid)))
                .map(AlertResponseDto::toVo);
    }

    @Override
    public Flux<AlertResponseVo> getCommonAlertStream() {
        // 공통 알림만 제공
        return alertSink.asFlux()
                .filter(AlertResponseDto::getCommonAlert)
                .map(AlertResponseDto::toVo);
    }
}
