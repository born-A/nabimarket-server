package org.prgrms.nabimarketbe.jpa.chatroom.repository;

import java.util.List;

import org.prgrms.nabimarketbe.jpa.chatroom.entity.QChatRoom;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.list.CardInfoListDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.list.ChatRoomInfoListDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.list.ChatRoomListWrapper;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.list.FromCardInfoListDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.list.ToCardInfoListDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.list.UserInfoListDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.single.CardInfoDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.single.ChatRoomInfoDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.single.ChatRoomInfoWrapper;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.single.FromCardInfoDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.single.ToCardInfoDTO;
import org.prgrms.nabimarketbe.jpa.chatroom.projection.single.UserInfoDTO;
import org.prgrms.nabimarketbe.jpa.suggestion.entity.QSuggestion;
import org.prgrms.nabimarketbe.jpa.util.CursorPaging;
import org.prgrms.nabimarketbe.jpa.util.QueryDslUtil;
import org.springframework.data.domain.Sort;

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
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom, CursorPaging {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ChatRoomInfoWrapper getChatRoomInfoById(
        Long chatRoomId,
        Long completeRequestId
    ) {
        ChatRoomInfoWrapper chatRoomInfo = jpaQueryFactory
            .select(
                Projections.fields(
                    ChatRoomInfoWrapper.class,
                    Projections.fields(
                        ChatRoomInfoDTO.class,
                        QChatRoom.chatRoom.fireStoreChatRoomPath.as("fireStoreChatRoomId"),
                        Projections.fields(
                            FromCardInfoDTO.class,
                            Projections.fields(
                                CardInfoDTO.class,
                                QSuggestion.suggestion.fromCard.cardId,
                                QSuggestion.suggestion.fromCard.item.itemName,
                                QSuggestion.suggestion.fromCard.thumbnail
                            ).as("cardInfo"),
                            Projections.fields(
                                UserInfoDTO.class,
                                QSuggestion.suggestion.fromCard.user.userId
                            ).as("userInfo")
                        ).as("fromCardInfo"),
                        Projections.fields(
                            ToCardInfoDTO.class,
                            Projections.fields(
                                CardInfoDTO.class,
                                QSuggestion.suggestion.toCard.cardId,
                                QSuggestion.suggestion.toCard.item.itemName,
                                QSuggestion.suggestion.toCard.thumbnail
                            ).as("cardInfo"),
                            Projections.fields(
                                UserInfoDTO.class,
                                QSuggestion.suggestion.toCard.user.userId
                            ).as("userInfo")
                        ).as("toCardInfo")
                    ).as("chatRoomInfo")
                )
            )
            .from(QChatRoom.chatRoom)
            .join(QSuggestion.suggestion)
            .on(QChatRoom.chatRoom.suggestion.suggestionId.eq(QSuggestion.suggestion.suggestionId))
            .where(QChatRoom.chatRoom.chatRoomId.eq(chatRoomId))
            .fetchOne();

        chatRoomInfo.getChatRoomInfo().updateFireStoreChatRoomId();
        chatRoomInfo.getChatRoomInfo().setCompleteRequestId(completeRequestId);

        return chatRoomInfo;
    }

    @Override
    public ChatRoomListWrapper getChatRoomList(
        Integer size,
        String cursorId,
        Long userId
    ) {
        List<ChatRoomInfoListDTO> chatRoomList = jpaQueryFactory
            .select(
                Projections.fields(
                    ChatRoomInfoListDTO.class,
                    QChatRoom.chatRoom.chatRoomId,
                    QChatRoom.chatRoom.createdDate.as("createdAt"),
                    Projections.fields(
                        FromCardInfoListDTO.class,
                        Projections.fields(
                            CardInfoListDTO.class,
                            QSuggestion.suggestion.fromCard.cardId,
                            QSuggestion.suggestion.fromCard.item.itemName,
                            QSuggestion.suggestion.fromCard.item.priceRange,
                            QSuggestion.suggestion.fromCard.thumbnail
                        ).as("cardInfo"),
                        Projections.fields(
                            UserInfoListDTO.class,
                            QSuggestion.suggestion.fromCard.user.userId
                        ).as("userInfo")
                    ).as("fromCardInfo"),
                    Projections.fields(
                        ToCardInfoListDTO.class,
                        Projections.fields(
                            CardInfoListDTO.class,
                            QSuggestion.suggestion.toCard.cardId,
                            QSuggestion.suggestion.toCard.item.itemName,
                            QSuggestion.suggestion.toCard.item.priceRange,
                            QSuggestion.suggestion.toCard.thumbnail
                        ).as("cardInfo"),
                        Projections.fields(
                            UserInfoListDTO.class,
                            QSuggestion.suggestion.toCard.user.userId
                        ).as("userInfo")
                    ).as("toCardInfo")
                )
            )
            .from(QChatRoom.chatRoom)
            .join(QSuggestion.suggestion)
            .on(QChatRoom.chatRoom.suggestion.suggestionId.eq(QSuggestion.suggestion.suggestionId))
            .where(
                cursorId(cursorId),
                owner(userId)
            )
            .orderBy(
                QueryDslUtil.getOrderSpecifier(
                    Sort.by(
                        Sort.Order.desc("createdDate"),
                        Sort.Order.desc("chatRoomId")
                    ),
                    QChatRoom.chatRoom
                )
            )
            .limit(size)
            .fetch();

        String nextCursor =
            chatRoomList.size() < size ? null : generateCursor(chatRoomList.get(chatRoomList.size() - 1));

        ChatRoomListWrapper chatRoomListWrapper = new ChatRoomListWrapper(chatRoomList, nextCursor);

        return chatRoomListWrapper;
    }

    @Override
    public BooleanExpression cursorId(String cursorId) {
        if (cursorId == null) {
            return null;
        }

        // 생성일자
        StringTemplate dateCursorTemplate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})",
            QChatRoom.chatRoom.createdDate,
            ConstantImpl.create("%Y%m%d%H%i%s")
        );

        // pk
        StringExpression pkCursorTemplate = StringExpressions.lpad(
            QChatRoom.chatRoom.chatRoomId.stringValue(),
            8,
            '0'
        );

        return dateCursorTemplate.concat(pkCursorTemplate).lt(cursorId);
    }

    private String generateCursor(ChatRoomInfoListDTO chatRoomInfoListDTO) {
        return chatRoomInfoListDTO.getCreatedAt().toString()    // 디폴트는 생성일자 최신순 정렬
            .replace("T", "")
            .replace("-", "")
            .replace(":", "")
            + String.format("%08d", chatRoomInfoListDTO.getChatRoomId());
    }

    private BooleanExpression owner(Long userId) {
        return QSuggestion.suggestion.fromCard.user.userId.eq(userId)
            .or(QSuggestion.suggestion.toCard.user.userId.eq(userId));
    }
}
