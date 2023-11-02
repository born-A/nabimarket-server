package org.prgrms.nabimarketbe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "G0001", "알 수 없는 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "G0002","잘못된 요청입니다.");

    private HttpStatus status;
    private String code;
    private String message;
}
