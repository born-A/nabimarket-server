package org.prgrms.nabimarketbe.global.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    private boolean success;

    private String code;

    private String message;
}
