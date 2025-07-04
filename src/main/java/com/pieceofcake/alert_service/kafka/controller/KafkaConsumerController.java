package com.pieceofcake.alert_service.kafka.controller;

import com.pieceofcake.alert_service.alert.application.AlertServiceImpl;
import com.pieceofcake.alert_service.alert.entity.enums.AlertType;
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

    @KafkaListener(topics = "test-topic", groupId = "funding-start-group", containerFactory = "stringEventListener")
    public void fundingStart(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received funding start event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.FUNDING_START);
    }

    @KafkaListener(topics = "success-funding-participant-alarm", groupId = "funding-success-group", containerFactory = "stringEventListener")
    public void fundingSuccess(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received funding success event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.FUNDING_SUCCESS);
    }

    @KafkaListener(topics = "end-funding-alarm", groupId = "funding-end-group", containerFactory = "stringEventListener")
    public void fundingEnd(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received funding end event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.FUNDING_END);
    }

    @KafkaListener(topics = "sell-piece-success-alarm", groupId = "piece-sell-success-group", containerFactory = "stringEventListener")
    public void pieceSellSuccess(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received piece sell success event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.PIECE_SELL_SUCCESS);
    }

    @KafkaListener(topics = "buy-piece-success-alarm", groupId = "piece-buy-success-group", containerFactory = "stringEventListener")
    public void pieceBuySuccess(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received piece buy success event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.PIECE_BUY_SUCCESS);
    }

    @KafkaListener(topics = "end-vote-alarm", groupId = "vote-end-group", containerFactory = "stringEventListener")
    public void voteEnd(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received vote end event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.VOTE_END);
    }

    @KafkaListener(topics = "success-auction-participant-alarm", groupId = "auction-success-group", containerFactory = "stringEventListener")
    public void auctionSuccess(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received auction success event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.AUCTION_SUCCESS);
    }

    @KafkaListener(topics = "update-funding-piece-count-alarm", groupId = "funding-count-group", containerFactory = "stringEventListener")
    public void fundingCountChange(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received funding count change event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.FUNDING_COUNT_CHANGE);
    }

    @KafkaListener(topics = "update-piece-price-alarm", groupId = "piece-price-group", containerFactory = "stringEventListener")
    public void piecePriceChange(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received piece price change event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.PIECE_PRICE_CHANGE);
    }

    @KafkaListener(topics = "update-product-status-alarm", groupId = "product-status-group", containerFactory = "stringEventListener")
    public void productStatusChange(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received product status change event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.PRODUCT_STATUS_CHANGE);
    }

    @KafkaListener(topics = "end-auction-alarm", groupId = "auction-end-group", containerFactory = "stringEventListener")
    public void auctionEnd(AlertKafkaEvent alertKafkaEvent) {
        log.info("Received auction end event: {}", alertKafkaEvent);
        alertService.getAlert(alertKafkaEvent, AlertType.AUCTION_END);
    }
}
