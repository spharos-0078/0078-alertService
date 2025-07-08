package com.pieceofcake.alert_service.alert.application;

import com.mongodb.client.model.changestream.OperationType;
import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import com.pieceofcake.alert_service.alert.entity.Alert;
import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import com.pieceofcake.alert_service.alert.infrastructure.AlertMongoRepository;
import com.pieceofcake.alert_service.kafka.event.AlertKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final MongoTemplate mongoTemplate;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final AlertMongoRepository alertMongoRepository;

    @Override
    public void getAlert(AlertKafkaEvent alertKafkaEvent, AlertType alertType) {
        log.info("Received alert event: {}", alertKafkaEvent);
        mongoTemplate.save(alertKafkaEvent.toEntity(alertType));
    }

    @Override
    public Flux<AlertResponseDto> getAlertByMemberUuid(String memberUuid) {
        log.debug("getAlertByMemberUuid 호출됨. memberUuid: {}", memberUuid);

        ChangeStreamOptions options;

        Flux<Alert> alertFlux;


        if (memberUuid == null || memberUuid.isBlank()) {
            log.debug("memberUuid가 없으므로 공용 알림(commonAlert=true)만 필터링합니다.");

            options = ChangeStreamOptions.builder()
                    .filter(Aggregation.newAggregation(
                            Aggregation.match(Criteria.where("operationType").is(OperationType.INSERT.getValue())),
                            Aggregation.match(Criteria.where("fullDocument.commonAlert").is(true))
                    )).build();
        } else {
            log.debug("memberUuid가 있으므로 공용 알림과 개인 알림을 필터링합니다. memberUuid: {}", memberUuid);

            options = ChangeStreamOptions.builder()
                    .filter(Aggregation.newAggregation(
                            Aggregation.match(Criteria.where("operationType").is(OperationType.INSERT.getValue())),
                            Aggregation.match(
                                    new Criteria().orOperator(
                                            Criteria.where("fullDocument.commonAlert").is(true),
                                            Criteria.where("fullDocument.commonAlert").is(false)
                                                    .and("fullDocument.memberUuid").is(memberUuid)
                                    )
                            )
                    )).build();
        }
        log.debug("ChangeStreamOptions 설정 완료. 컬렉션: alert_message_entity");

        return reactiveMongoTemplate.changeStream("alert_message_entity", options, Document.class)
                .doOnSubscribe(subscription -> log.debug("ChangeStream 구독 시작"))
                .doOnNext(event -> log.debug("ChangeStream 이벤트 수신: {}", event))
                .map(ChangeStreamEvent::getBody)
                .map(document -> {
                    log.debug("ChangeStream 문서 변환 시작: {}", document);
                    AlertResponseDto responseDto = AlertResponseDto.builder()
                            .key(document.getString("key"))
                            .message(document.getString("message"))
                            .memberUuid(document.getString("memberUuid"))
                            .alertType(AlertType.valueOf(document.getString("alertType")))
                            .build();
                    log.debug("AlertResponseDto 생성 완료: {}", responseDto);
                    return responseDto;
                })
                .doOnError(error -> log.error("ChangeStream 처리 중 오류 발생", error))
                .doOnComplete(() -> log.debug("ChangeStream 처리 완료"));

    }

}


