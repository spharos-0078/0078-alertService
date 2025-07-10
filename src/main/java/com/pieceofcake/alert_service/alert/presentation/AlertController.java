package com.pieceofcake.alert_service.alert.presentation;

import com.pieceofcake.alert_service.alert.application.AlertService;
import com.pieceofcake.alert_service.alert.dto.out.AlertResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @Operation(
            summary = "실시간 알림 스트림 제공",
            description = "SSE를 통해 실시간 알림 스트림을 제공합니다. \n\n" +
                    "- memberUuid가 제공되지 않으면 commonAlert가 true인 공용 알림만 스트리밍합니다. \n\n" +
                    "- memberUuid가 제공되면 commonAlert가 true인 공용 알림과 commonAlert가 false면서 해당 memberUuid와 일치하는 개인 알림을 스트리밍합니다. \n\n" +
                    "### 공용 알람\n" +
                    "- **FUNDING_START**: 공모 시작\n" +
                    "- **FUNDING_END**: 공모 종료\n" +
                    "- **FUNDING_COUNT_CHANGE**: 공모 조각 개수 변경\n" +
                    "- **PIECE_PRICE_CHANGE**: 조각 가격 변경\n" +
                    "- **PRODUCT_STATUS_CHANGE**: 상품 상태 변경\n" +
                    "- **AUCTION_END**: 경매 종료\n\n" +
                    "### 개인용 알람\n" +
                    "- **FUNDING_SUCCESS**: 공모 참여 성공\n" +
                    "- **PIECE_SELL_SUCCESS**: 조각 판매 성공\n" +
                    "- **PIECE_BUY_SUCCESS**: 조각 구매 성공\n" +
                    "- **VOTE_END**: 투표 종료\n" +
                    "- **AUCTION_SUCCESS**: 경매 구매 성공\n" +
                    "- **REPLY_COMMENT**: 대댓글 생성\n\n\n" +
                    "각 key에는, 서비스의 종류에 따라 productUuid, fundingUuid, pieceProductUuid, voteUuid, auctionUuid, parentReplyUuid 값이 전달됩니다. "
    )
    @GetMapping(value = "/stream/new", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AlertResponseDto> getAlertByMemberUuid(
            @RequestParam(required = false) String memberUuid
    ) {
        return alertService.getAlertByMemberUuid(memberUuid).subscribeOn(Schedulers.boundedElastic());
    }
}





