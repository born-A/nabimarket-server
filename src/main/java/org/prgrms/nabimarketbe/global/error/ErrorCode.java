package org.prgrms.nabimarketbe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "G0001", "알 수 없는 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "G0002","잘못된 요청입니다."),

    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U0001", "존재하지 않는 사용자입니다."),

    CARD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "C0001", "존재하지 않는 카드입니다."),
    CARD_SUGGESTION_MYSELF_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C0002", "자신의 카드에는 제안을 할 수 없습니다."),

    CATEGORY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "CA0001", "존재하지 않는 카테고리입니다.");

    private HttpStatus status;
    private String code;
    private String message;
}