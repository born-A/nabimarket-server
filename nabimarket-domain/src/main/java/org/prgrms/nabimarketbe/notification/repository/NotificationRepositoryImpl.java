package org.prgrms.nabimarketbe.notification.repository;

import java.util.List;

import org.prgrms.nabimarketbe.notification.entity.QNotification;
import org.prgrms.nabimarketbe.notification.projection.NotificationDetailResponseDTO;
import org.prgrms.nabimarketbe.notification.wrapper.NotificationPagingResponseDTO;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.util.CursorPaging;
import org.prgrms.nabimarketbe.util.QueryDslUtil;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom, CursorPaging {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public NotificationPagingResponseDTO getNotificationsByIsRead(
        User receiver,
        Boolean isRead,
        Integer size,
        String cursorId,
        Pageable pageable
    ) {
        List<NotificationDetailResponseDTO> notificationList = jpaQueryFactory.select(
                Projections.fields(
                    NotificationDetailResponseDTO.class,
                    QNotification.notification.notificationId,
                    QNotification.notification.content,
                    QNotification.notification.isRead,
                    QNotification.notification.card.cardId,
                    QNotification.notification.createdDate.as("createdAt"),
                    QNotification.notification.modifiedDate.as("modifiedAt")
                )
            )
            .from(QNotification.notification)
            .where(
                receiverEquals(receiver),
                cursorId(cursorId),
                isReadEquals(isRead)
            )
            .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort(), QNotification.notification))
            .limit(pageable.getPageSize())
            .fetch();

        String nextCursorId = notificationList.size() < pageable.getPageSize() ?
            null : generateCursor(notificationList.get(notificationList.size() - 1));

        return new NotificationPagingResponseDTO(notificationList, nextCursorId);
    }

    @Override
    public BooleanExpression cursorId(String cursorId) {
        if (cursorId == null) {
            return null;
        }

        // 생성일자
        StringTemplate dateCursorTemplate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})",
            QNotification.notification.createdDate,
            ConstantImpl.create("%Y%m%d%H%i%s")
        );

        // pk
        StringExpression pkCursorTemplate = StringExpressions.lpad(
            QNotification.notification.notificationId.stringValue(),
            8,
            '0'
        );

        return dateCursorTemplate.concat(pkCursorTemplate).lt(cursorId);
    }

    private BooleanExpression receiverEquals(User receiver) {
        return QNotification.notification.receiver.eq(receiver);
    }

    private String generateCursor(NotificationDetailResponseDTO notificationDetailResponseDTO) {
        return notificationDetailResponseDTO.getCreatedAt().toString()    // 디폴트는 생성일자 최신순 정렬
            .replace("T", "")
            .replace("-", "")
            .replace(":", "")
            + String.format("%08d", notificationDetailResponseDTO.getNotificationId());
    }

    private BooleanExpression isReadEquals(Boolean isRead) {
        if (isRead == null) {
            return null;
        }

        return QNotification.notification.isRead.eq(isRead);
    }
}
