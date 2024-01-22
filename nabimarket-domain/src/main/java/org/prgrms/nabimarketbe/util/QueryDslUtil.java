package org.prgrms.nabimarketbe.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;

public class QueryDslUtil {
    /**
     * 정렬을 기준을 반환하기 위한 메소드.
     * 여러 정렬 조건들을 반환할 수 있다.
     *
     * @param sort Sort 객체
     * @param parent compileQuerydsl 빌드를 통해서 생성된 Q타입 클래스의 객체(Sort의 대상이 되는 Q타입 클래스 객체를 전달한다.)
     * @return
     */
    public static OrderSpecifier[] getOrderSpecifier(
        Sort sort,
        Path<?> parent
    ) {
        List<OrderSpecifier> orders = new ArrayList<>();

        for (Sort.Order order : sort) { // Sort에 여러 정렬 기준이 담겨 올 수 있음
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            orders.add(QueryDslUtil.getSortedColumn(direction, parent, order.getProperty()));
        }

        return orders.toArray(OrderSpecifier[]::new);
    }

    /**
     * Order, Path, fieldName을 전달하면 OrderSpecifier 객체를 리턴하는 Util 클래스.
     * Sort시 마다 사용할 수 있도록 한다.
     *
     * @param order
     * @param parent compileQuerydsl 빌드를 통해서 생성된 Q타입 클래스의 객체(Sort의 대상이 되는 Q타입 클래스 객체를 전달한다.)
     * @param fieldName
     * @return OrderSpecifier 객체
     */
    public static OrderSpecifier<?> getSortedColumn(
        Order order,
        Path<?> parent,
        String fieldName
    ) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);

        return new OrderSpecifier(order, fieldPath);
    }
}
