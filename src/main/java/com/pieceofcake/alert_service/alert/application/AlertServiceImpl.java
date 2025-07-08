package com.pieceofcake.alert_service.alert.application;

import com.mongodb.client.model.changestream.OperationType;
import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import com.pieceofcake.alert_service.alert.entity.Alert;
import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
import com.pieceofcake.alert_service.alert.infrastructure.AlertMongoRepository;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
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
import reactor.core.publisher.Sinks;
import reactor.core.Scannable;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final MongoTemplate mongoTemplate;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final AlertMongoRepository alertMongoRepository;

    @Override
    public void getAlert(AlertKafkaEvent alertKafkaEvent, AlertType alertType) {
        log.info("@@@@@@@123");
        mongoTemplate.save(alertKafkaEvent.toEntity(alertType));
        log.info("@@@@@@@");
    }

    @Override
    public Flux<AlertResponseDto> getAlertByMemberUuid(String memberUuid) {

        ChangeStreamOptions options;

        Flux<Alert> alertFlux;


        if (memberUuid == null || memberUuid.isBlank()) {
            options = ChangeStreamOptions.builder()
                    .filter(Aggregation.newAggregation(
                            Aggregation.match(Criteria.where("operationType").is(OperationType.INSERT.getValue())),
                            Aggregation.match(Criteria.where("fullDocument.commonAlert").is(true))
                    )).build();
        } else {
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

        return reactiveMongoTemplate.changeStream("alert_message_entity", options, Document.class)
                .map(ChangeStreamEvent::getBody)
                .map(document -> AlertResponseDto.builder()
                        .key(document.getString("key"))
                        .message(document.getString("message"))
                        .memberUuid(document.getString("memberUuid"))
                        .alertType(AlertType.valueOf(document.getString("alertType")))
                        .build());

//        if (memberUuid == null || memberUuid.isBlank()) {
//            // 공용 알람만 조회
//            return alertMongoRepository.findByCommonAlertTrue().map(alert ->
//                    AlertResponseDto.builder()
//                            .key(alert.getKey())
//                            .message(alert.getMessage())
//                            .memberUuid(alert.getMemberUuid())
//                            .alertType(alert.getAlertType())
//                            .build()
//            );
//        } else {
//            // 개인 알림 + 공용 알림 둘 다 merge
//            Flux<Alert> personalAlerts = alertMongoRepository.findByMemberUuid(memberUuid);
//            Flux<Alert> commonAlerts = alertMongoRepository.findByCommonAlertTrue();
//        return Flux.merge(personalAlerts, commonAlerts).map(alert ->
//                AlertResponseDto.builder()
//                        .key(alert.getKey())
//                        .message(alert.getMessage())
//                        .memberUuid(alert.getMemberUuid())
//                        .alertType(alert.getAlertType())
//                        .build()
//                );
//        }
    }

}


