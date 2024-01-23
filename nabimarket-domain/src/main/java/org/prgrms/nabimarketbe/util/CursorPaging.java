package org.prgrms.nabimarketbe.util;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface CursorPaging {
    /**
     * cursorId를 받아 cursor Id로 조건절을 만드는 메소드
     * @param cursorId
     * @return
     */
    BooleanExpression cursorId(String cursorId);
}
