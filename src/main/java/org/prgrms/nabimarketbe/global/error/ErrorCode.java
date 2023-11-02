package org.prgrms.nabimarketbe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN("G0001", "알 수 없는 오류가 발생했습니다."),
    INVALID_REQUEST("G0002","잘못된 요청입니다.");

    private String code;
    private String message;
}
