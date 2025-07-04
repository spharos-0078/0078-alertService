package com.pieceofcake.alert_service.alert.presentation;

import com.pieceofcake.alert_service.alert.application.AlertService;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AlertResponseVo>> streamAlerts(
            @RequestParam(required = false) String memberUuid
    ) {
        Flux<AlertResponseVo> alertStream;

        if (memberUuid != null && !memberUuid.isEmpty()) {
            alertStream = alertService.getAlertStreamForMember(memberUuid);
        } else {
            alertStream = alertService.getCommonAlertStream();
        }

        return alertStream
                .map(alertVo -> ServerSentEvent.<AlertResponseVo>builder()
                        .event("alert")
                        .data(alertVo)
                        .build());
    }
}
