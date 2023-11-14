package org.prgrms.nabimarketbe.domain.card.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardStatusUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardDetail;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionAvailableCardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.category.repository.CategoryRepository;
import org.prgrms.nabimarketbe.domain.dibs.repository.DibRepository;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.item.repository.ItemRepository;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    private final CardImageRepository cardImageRepository;

    private final UserRepository userRepository;

    private final CheckService checkService;

    private final DibRepository dibRepository;

    @Transactional
    public CardResponseDTO<CardCreateResponseDTO> createCard(
            String token,
            CardCreateRequestDTO cardCreateRequestDTO
    ) {
        User user = userRepository.findById(checkService.parseToken(token))
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findCategoryByCategoryName(cardCreateRequestDTO.category())
                .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));

        Item item = cardCreateRequestDTO.toItemEntity(category);

        Card card = cardCreateRequestDTO.toCardEntity(item, user);

        CardImage thumbNail = new CardImage(cardCreateRequestDTO.thumbNailImage(), card);

        // images 비어있을 경우..
        List<CardImage> images = Optional.ofNullable(cardCreateRequestDTO.images())
            .orElse(new ArrayList<>())
            .stream()
            .map(i -> i.toCardImageEntity(card))
            .toList();

        List<CardImage> newCardImages = addThumbNail(images, thumbNail);

        Item savedItem = itemRepository.save(item);
        Card savedCard = cardRepository.save(card);
        List<CardImage> savedCardImages = cardImageRepository.saveAll(newCardImages);    // TODO: images bulk insert로 전환

        CardCreateResponseDTO cardCreateResponseDTO = CardCreateResponseDTO.of(
            savedCard,
            savedItem,
            savedCardImages
        );

        return new CardResponseDTO<>(cardCreateResponseDTO);
    }

    @Transactional
    public CardResponseDTO<CardUpdateResponseDTO> updateCardById(
        String token,
        Long cardId,
        CardUpdateRequestDTO cardUpdateRequestDTO
    ) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_MATCHED));

        if (!checkService.isEqual(token, card.getUser().getUserId())) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }

        Category category = categoryRepository.findCategoryByCategoryName(cardUpdateRequestDTO.category())
            .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));

        Item item = card.getItem();

        item.updateItem(
            cardUpdateRequestDTO.itemName(),
            cardUpdateRequestDTO.priceRange(),
            category
        );

        card.updateCard(
            cardUpdateRequestDTO.cardTitle(),
            cardUpdateRequestDTO.thumbNailImage(),
            cardUpdateRequestDTO.poke(),
            cardUpdateRequestDTO.content(),
            cardUpdateRequestDTO.tradeType(),
            cardUpdateRequestDTO.tradeArea()
        );

        cardImageRepository.deleteAllByCard(card);

        List<CardImage> cardImages = cardUpdateRequestDTO.images().stream()
            .map(i -> i.toCardImageEntity(card))
            .toList();

        List<CardImage> savedCardImages = cardImageRepository.saveAll(cardImages);

        CardUpdateResponseDTO cardUpdateResponseDTO = CardUpdateResponseDTO.of(
            card,
            item,
            savedCardImages
        );

        return new CardResponseDTO<>(cardUpdateResponseDTO);
    }

   @Transactional(readOnly = true)
   public CardSingleReadResponseDTO getCardById(
       String token,
       Long cardId
   ) {
       Long userId = checkService.parseToken(token);
       User user = userRepository.findById(userId)
           .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

       Card card = cardRepository.findById(cardId)
               .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

       if (!user.getUserId().equals(card.getUser().getUserId())) {
           card.updateViewCount();
       }

       List<CardImage> cardImages = cardImageRepository.findAllByCard(card);

       boolean isMyDib = dibRepository.existsDibByCardAndUser(card, user);

       CardDetail cardDetail = CardDetail.of(
           card,
           cardImages,
           isMyDib
       );

       UserSummaryResponseDTO userSummaryResponseDTO = UserSummaryResponseDTO.from(user);

       CardResponseDTO<CardDetail> cardInfo = new CardResponseDTO<>(cardDetail);
       UserResponseDTO<UserSummaryResponseDTO> userInfo = new UserResponseDTO<>(userSummaryResponseDTO);

       return CardSingleReadResponseDTO.of(
           cardInfo,
           userInfo
       );
   }

    @Transactional(readOnly = true)
    public CardListReadPagingResponseDTO getCardsByCondition(
            CategoryEnum category,
            PriceRange priceRange,
            List<CardStatus> status,
            String title,
            String cursorId,
            Integer size
    ) {
        return cardRepository.getCardsByCondition(
                category,
                priceRange,
                status,
                title,
                cursorId,
                size
        );
    }

    @Transactional(readOnly = true)
    public CardListResponseDTO<SuggestionAvailableCardResponseDTO> getSuggestionAvailableCards(
            String token,
            Long cardId
    ) {
        User requestUser = userRepository.findById(checkService.parseToken(token))
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        Card suggestionTargetCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (requestUser.getUserId().equals(suggestionTargetCard.getUser().getUserId())) {
            throw new BaseException(ErrorCode.CARD_SUGGESTION_MYSELF_ERROR);
        }

        List<SuggestionAvailableCardResponseDTO> cardListResponse = cardRepository.getSuggestionAvailableCards(
                requestUser.getUserId(),
                suggestionTargetCard.getItem().getPriceRange(),
                suggestionTargetCard.getPokeAvailable()
        );

        return new CardListResponseDTO<>(cardListResponse);
    }

    @Transactional(readOnly = true)
    public CardListReadPagingResponseDTO getMyCardsByStatus(
            String token,
            CardStatus status,
            String cursorId,
            Integer size
    ) {
        User user = userRepository.findById(checkService.parseToken(token))
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        return cardRepository.getMyCardsByStatus(
                user,
                status,
                cursorId,
                size
        );
    }

    @Transactional
    public void updateCardStatusById(
            String token,
            Long cardId,
            CardStatusUpdateRequestDTO cardStatusUpdateRequestDTO
    ) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (!checkService.isEqual(token, card.getUser().getUserId())) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }

        switch (cardStatusUpdateRequestDTO.cardStatus()) {
            case TRADE_AVAILABLE -> card.updateCardStatusToTradeAvailable();
            case RESERVED -> card.updateCardStatusToReserved();
            case TRADE_COMPLETE -> card.updateCardStatusToTradeComplete();
        }
    }

    @Transactional
    public void deleteCardById(
            String token,
            Long cardId
    ) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (!checkService.isEqual(token, card.getUser().getUserId())) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }

        cardRepository.delete(card);
    }

    private List<CardImage> addThumbNail(List<CardImage> cardImages, CardImage thumbnail) {
        List<CardImage> newCardImages = new ArrayList<>(cardImages);
        newCardImages.add(0, thumbnail);

        return newCardImages;
    }
}
