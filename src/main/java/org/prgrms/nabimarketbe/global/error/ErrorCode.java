package org.prgrms.nabimarketbe.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "G0001", "알 수 없는 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "G0002", "잘못된 요청입니다."),
    EXTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G0003", "외부 서버 오류입니다."),
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U0001", "존재하지 않는 사용자입니다."),
    USER_NOT_MATCHED(HttpStatus.INTERNAL_SERVER_ERROR, "UOO02", "적합한 사용자가 아닙니다."),
    USER_NICKNAME_NOT_UNIQUE(HttpStatus.BAD_REQUEST, "UOO03", "이미 존재하는 닉네임입니다."),
    USER_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "U0004", "만료된 토큰입니다."),
    USER_TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "U0005", "잘못된 Jwt 서명입니다."),
    USER_REFRESH_TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "U0005", "잘못된 리프레시 토큰 서명입니다."),
    USER_TOKEN_NOT_SUPPORTED(HttpStatus.UNAUTHORIZED, "U0006", "지원하지 않는 토큰입니다."),
    USER_TOKEN_CLAIM_NULL(HttpStatus.UNAUTHORIZED, "U0007", "유저토큰의 claim이 null입니다."),
    CARD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "C0001", "존재하지 않는 카드입니다."),
    DIB_NOT_FOUND(HttpStatus.BAD_REQUEST, "D0001", "존재하지 않는 찜입니다."),
    DIB_MYSELF_ERROR(HttpStatus.BAD_REQUEST, "D0002", "자신의 카드는 찜할 수 없습니다."),
    DIB_DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, "D0003", "이미 찜한 카드는 또 찜할 수 없습니다."),
    SUGGESTION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "S0001", "존재하지 않는 제안입니다."),
    SUGGESTION_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "S0002", "잘못된 제안 타입입니다."),
    SUGGESTION_NOT_ACCEPTED(HttpStatus.BAD_REQUEST, "S0003", "수락된 제안이 아닙니다."),
    SUGGESTION_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "S0004", "제안이 이미 존재합니다"),
    CARD_SUGGESTION_MYSELF_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C0002", "자신의 카드에는 제안을 할 수 없습니다."),
    CARD_TRADE_COMPLETE(HttpStatus.INTERNAL_SERVER_ERROR, "C0003", "이미 거래완료된 카드입니다."),
    CARD_DEACTIVATED(HttpStatus.INTERNAL_SERVER_ERROR, "C0004", "삭제된 카드입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "CA0001", "존재하지 않는 카테고리입니다."),
    COMPLETE_REQUEST_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "CR0001", "존재하지 않는 거래 성사 요청입니다."),
    COMPLETE_REQUEST_MYSELF_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CR0002", "자신의 카드에는 거래 성사 요청을 할 수 없습니다."),
    COMPLETE_REQUEST_FROM_CARD_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "CR0002", "거래 성사 요청을 이미 보낸 카드입니다."),
    COMPLETE_REQUEST_TO_CARD_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "CR0002", "거래 성사 요청을 이미 받은 카드입니다."),
    COMPLETE_REQUEST_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "CR0003", "이미 거래 성사 요청이 존재합니다"),
    BATCH_INSERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "B0001", "이미지 저장 중에 문제가 발생했습니다."),
    INVALID_ORDER_CONDITION(HttpStatus.INTERNAL_SERVER_ERROR, "O0001", "유효하지 않은 정렬 조건입니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "CH0001", "채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "CH0004", "이미 생성된 채팅방이 존재합니다."),
    UPLOAD_FILE_NAME_TOO_LONG(HttpStatus.INTERNAL_SERVER_ERROR, "UL0001", "파일 이름의 길이가 제한된 길이를 넘었습니다.");

    private HttpStatus status;
    private String code;
    private String message;
}
