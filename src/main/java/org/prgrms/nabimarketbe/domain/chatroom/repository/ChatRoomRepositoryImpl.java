package org.prgrms.nabimarketbe.domain.chatroom.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.CardInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.ChatRoomInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.ChatRoomInfoWrapper;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.FromCardInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.ToCardInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.UserInfoDTO;
import org.prgrms.nabimarketbe.global.util.CursorPaging;

import static org.prgrms.nabimarketbe.domain.suggestion.entity.QSuggestion.suggestion;
import static org.prgrms.nabimarketbe.domain.chatroom.entity.QChatRoom.chatRoom;

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
                        chatRoom.fireStoreChatRoomPath.as("fireStoreChatRoomId"),
                        Projections.fields(
                            FromCardInfoDTO.class,
                            Projections.fields(
                                CardInfoDTO.class,
                                suggestion.fromCard.cardId,
                                suggestion.fromCard.item.itemName,
                                suggestion.fromCard.thumbnail
                            ).as("cardInfo"),
                            Projections.fields(
                                UserInfoDTO.class,
                                suggestion.fromCard.user.userId
                            ).as("userInfo")
                        ).as("fromCardInfo"),
                        Projections.fields(
                            ToCardInfoDTO.class,
                            Projections.fields(
                                CardInfoDTO.class,
                                suggestion.toCard.cardId,
                                suggestion.toCard.item.itemName,
                                suggestion.toCard.thumbnail
                            ).as("cardInfo"),
                            Projections.fields(
                                UserInfoDTO.class,
                                suggestion.toCard.user.userId
                            ).as("userInfo")
                        ).as("toCardInfo")
                    ).as("chatRoomInfo")
                )
            )
            .from(chatRoom)
            .join(suggestion).on(chatRoom.suggestion.suggestionId.eq(suggestion.suggestionId))
            .where(chatRoom.chatRoomId.eq(chatRoomId))
            .fetchOne();

        chatRoomInfo.getChatRoomInfo().setCompleteRequestId(completeRequestId);

        return chatRoomInfo;
    }

    @Override
    public BooleanExpression cursorId(String cursorId) {
        return null;
    }
}
