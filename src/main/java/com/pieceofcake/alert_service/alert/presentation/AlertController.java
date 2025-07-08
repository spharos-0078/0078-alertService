package com.pieceofcake.alert_service.alert.presentation;

import com.pieceofcake.alert_service.alert.application.AlertService;
import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping(value = "/stream/new", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AlertResponseDto> getAlertByMemberUuid(
            @RequestParam(required = false) String memberUuid
    ) {
        return alertService.getAlertByMemberUuid(memberUuid).subscribeOn(Schedulers.boundedElastic());
    }
}





