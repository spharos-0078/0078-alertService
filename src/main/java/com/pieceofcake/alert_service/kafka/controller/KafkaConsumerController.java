package com.pieceofcake.alert_service.kafka.controller;

import com.pieceofcake.alert_service.alert.application.AlertServiceImpl;
import com.pieceofcake.alert_service.kafka.event.AlertKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaConsumerController {

    private final AlertServiceImpl alertService;

//    @KafkaListener(topics = "auction-start", groupId = "auction-start-group")
//    public void auctionStartEvent(KafkaAlertEvent kafkaAlertEvent) {
//        log.info("Received product Create event: {}", kafkaAlertEvent);
//        alertService.getAlert(kafkaAlertEvent);
//    }

    @KafkaListener(topics = "test-topic", groupId = "piece-group", containerFactory = "stringEventListener")
    public void marketPriceUpdateEvent(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received market price update event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent);
    }
}
