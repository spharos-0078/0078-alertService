package com.pieceofcake.alert_service.alert.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertType {

    // 유저 알람
    FUNDING_START("공모 시작"),
    FUNDING_SUCCESS("공모 참여 성공"),
    FUNDING_END("공모 종료"),
    PIECE_SELL_SUCCESS("조각 판매 성공"),
    PIECE_BUY_SUCCESS("조각 구매 성공"),
    VOTE_END("투표 종료"),
    AUCTION_SUCCESS("경매 구매 성공"),
    REPLY_COMMENT("대댓글 생성"),

    // 프론트용 알람
    FUNDING_COUNT_CHANGE("공모 조각 개수 변경"),
    PIECE_PRICE_CHANGE("조각 가격 변경"),
    PRODUCT_STATUS_CHANGE("상품 상태 변경"),

    AUCTION_END("경매 종료");

    private final String description;

    @JsonValue
    public String getName() {
        return this.name();
    }
}
