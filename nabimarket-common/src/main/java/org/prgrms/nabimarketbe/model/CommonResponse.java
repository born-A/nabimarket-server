package org.prgrms.nabimarketbe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponse {
    SUCCESS("SUCCESS", "성공하였습니다."),
    FAIL("FAIL", "실패하였습니다.");

    private String code;

    private String msg;
}
