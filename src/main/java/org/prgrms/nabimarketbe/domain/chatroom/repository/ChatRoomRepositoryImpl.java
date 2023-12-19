package org.prgrms.nabimarketbe.domain.chatroom.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.CardInfoListDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.ChatRoomInfoListDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.FromCardInfoListDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.ToCardInfoListDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.UserInfoListDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.CardInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.ChatRoomInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.ChatRoomInfoWrapper;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.FromCardInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.ToCardInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.single.UserInfoDTO;
import org.prgrms.nabimarketbe.domain.chatroom.dto.response.list.ChatRoomListWrapper;
import org.prgrms.nabimarketbe.global.util.CursorPaging;
import org.prgrms.nabimarketbe.global.util.QueryDslUtil;
import org.springframework.data.domain.Sort;

import java.util.List;

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
                    chatRoom.chatRoomId,
                    chatRoom.createdDate.as("createdAt"),
                    Projections.fields(
                        FromCardInfoListDTO.class,
                        Projections.fields(
                            CardInfoListDTO.class,
                            suggestion.fromCard.cardId,
                            suggestion.fromCard.item.itemName,
                            suggestion.fromCard.item.priceRange,
                            suggestion.fromCard.thumbnail
                        ).as("cardInfo"),
                        Projections.fields(
                            UserInfoListDTO.class,
                            suggestion.fromCard.user.userId
                        ).as("userInfo")
                    ).as("fromCardInfo"),
                    Projections.fields(
                        ToCardInfoListDTO.class,
                        Projections.fields(
                            CardInfoListDTO.class,
                            suggestion.toCard.cardId,
                            suggestion.toCard.item.itemName,
                            suggestion.toCard.item.priceRange,
                            suggestion.toCard.thumbnail
                        ).as("cardInfo"),
                        Projections.fields(
                            UserInfoListDTO.class,
                            suggestion.toCard.user.userId
                        ).as("userInfo")
                    ).as("toCardInfo")
                )
            )
            .from(chatRoom)
            .join(suggestion).on(chatRoom.suggestion.suggestionId.eq(suggestion.suggestionId))
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
                    chatRoom
                )
            )
            .limit(size)
            .fetch();

        String nextCursor = chatRoomList.size() < size ? null : generateCursor(chatRoomList.get(chatRoomList.size() - 1));

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
                chatRoom.createdDate,
                ConstantImpl.create("%Y%m%d%H%i%s")
        );

        // pk
        StringExpression pkCursorTemplate = StringExpressions.lpad(
                chatRoom.chatRoomId.stringValue(),
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
        return suggestion.fromCard.user.userId.eq(userId)
                .or(suggestion.toCard.user.userId.eq(userId));
    }
}
