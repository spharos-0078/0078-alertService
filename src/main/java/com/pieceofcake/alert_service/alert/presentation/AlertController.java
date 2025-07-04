package com.pieceofcake.alert_service.alert.presentation;

import com.pieceofcake.alert_service.alert.application.AlertService;
import com.pieceofcake.alert_service.alert.vo.out.AlertResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "알림 스트림 제공",
            description = "SSE를 통해 실시간 알림 스트림을 제공합니다. \n\n" +
                    "- memberUuid가 제공되지 않으면 공용 알림만 스트리밍하고, \n\n" +
                    "- memberUuid가 제공되면 해당 사용자의 개인 알림과 공용 알림을 모두 스트리밍합니다.\n\n" +
                    "- AlertType = [펀딩 시작, 펀딩 성공, 펀딩 종료, 조각 판매 성공, 조각 구매 성공, 투표 종료, 상위입찰 성공, 펀딩 조각 개수 변경, 조각 가격 변경, 상품 상태 변경, 경매 종료]"
    )
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AlertResponseVo>> streamAlerts(
            @Parameter(
                    description = "회원 UUID - 제공 시 해당 회원의 개인 알림과 공용 알림 모두 수신, " +
                            "미제공 시 공용 알림만 수신",
                    required = false,
                    example = "member-uuid-1234"
            )
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
