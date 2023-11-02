package org.prgrms.nabimarketbe.global.util.model;

public record MyCommonResult<T>(
	String code,
	String msg,
	T data
) {
}
