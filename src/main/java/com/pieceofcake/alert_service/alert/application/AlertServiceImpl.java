package com.pieceofcake.alert_service.alert.application;

import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
import com.pieceofcake.alert_service.kafka.event.AlertKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.Scannable;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

//    private final Sinks.Many<AlertResponseDto> alertSink = Sinks.many().multicast().onBackpressureBuffer();
    private final AtomicReference<Sinks.Many<AlertResponseDto>> sinkRef =
        new AtomicReference<>(Sinks.many().replay().latest());

    /**
     * 현재 sink를 반환. TERMINATED 상태면 새로 만든 뒤 리턴
     */
    private Sinks.Many<AlertResponseDto> getOrCreateSink() {
        Sinks.Many<AlertResponseDto> sink = sinkRef.get();
        if (Boolean.TRUE.equals(sink.scan(Scannable.Attr.TERMINATED))) {
            log.info("[SSE] 기존 sink가 TERMINATED 상태 감지, 새로운 sink 생성");
            Sinks.Many<AlertResponseDto> fresh = Sinks.many().replay().latest();
            sinkRef.set(fresh);
            return fresh;
        }
        return sink;
    }

    @Override
    public AlertResponseDto getAlert(AlertKafkaEvent alertKafkaEvent, AlertType alertType) {
        AlertResponseDto responseDto = AlertResponseDto.of(
                alertKafkaEvent.getKey(),
                alertKafkaEvent.getMessage(),
                alertType,
                alertKafkaEvent.getMemberUuid(),
                alertKafkaEvent.getCommonAlert()
        );

//        // 알람 발생 시 Sink에 이벤트 발행
//        alertSink.tryEmitNext(responseDto);
        // 이벤트 발행 전 sink 상태 체크 및 교체
        Sinks.Many<AlertResponseDto> sink = getOrCreateSink();
        Sinks.EmitResult result = sink.tryEmitNext(responseDto);
        if (result.isFailure()) {
            log.error("[SSE] 이벤트 발행 실패: {}", result);
        }

        return responseDto;
    }

    /**
     * 공통 로깅/에러처리를 붙이는 메서드
     */
    private Flux<AlertResponseVo> decorate(Flux<AlertResponseDto> flux) {
        return flux
                .doOnSubscribe(sub -> log.info("[SSE] 구독 시작"))
                .doOnCancel(() -> log.info("[SSE] 구독 취소"))
                .doOnComplete(() -> log.info("[SSE] 구독 정상 종료"))
                .doOnError(e -> {
                    if (e instanceof IOException) {
                        log.info("[SSE] 클라이언트 연결 종료 감지 (무시)");
                    } else {
                        log.error("[SSE] SSE 스트림 에러", e);
                    }
                })
                .map(AlertResponseDto::toVo);
    }

    @Override
    public Flux<AlertResponseVo> getAlertStream() {
        // Sink로부터 Flux를 얻어 AlertResponseVo로 변환
//        return alertSink.asFlux().map(AlertResponseDto::toVo);
        return decorate(getOrCreateSink().asFlux());
    }

    @Override
    public Flux<AlertResponseVo> getMemberAlertStream(String memberUuid) {
//        // 특정 회원의 알림과 공통 알림 모두 제공
//        return alertSink.asFlux()
//                .filter(dto -> dto.getCommonAlert() ||
//                        (dto.getMemberUuid() != null && dto.getMemberUuid().equals(memberUuid)))
//                .map(AlertResponseDto::toVo);
        Flux<AlertResponseDto> filtered = getOrCreateSink().asFlux()
                .filter(dto ->
                        dto.getCommonAlert()
                                || (dto.getMemberUuid() != null && dto.getMemberUuid().equals(memberUuid))
                );
        return decorate(filtered);
    }

    @Override
    public Flux<AlertResponseVo> getCommonAlertStream() {
//        // 공통 알림만 제공
//        return alertSink.asFlux()
//                .filter(AlertResponseDto::getCommonAlert)
//                .map(AlertResponseDto::toVo);
        Flux<AlertResponseDto> filtered = getOrCreateSink().asFlux()
                .filter(AlertResponseDto::getCommonAlert);
        return decorate(filtered);
    }
}
