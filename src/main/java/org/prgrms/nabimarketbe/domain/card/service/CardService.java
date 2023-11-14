package org.prgrms.nabimarketbe.domain.card.service;

import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardStatusUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardResponseDTO;
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
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.item.repository.ItemRepository;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
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

        List<CardImage> images = cardCreateRequestDTO.images().stream()
            .map(i -> i.toCardImageEntity(card))
            .toList();

        Item savedItem = itemRepository.save(item);
        Card savedCard =  cardRepository.save(card);
        List<CardImage> savedCardImages = cardImageRepository.saveAll(images);    // TODO: images bulk insert로 전환

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

//    @Transactional
//    public CardSingleReadResponseDTO getCardById(Long cardId) {
//        Card card = cardRepository.findById(cardId)
//                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));
//
//        card.updateViewCount();
//
//        Item item = card.getItem();
//
//        List<CardImage> cardImages = cardImageRepository.findAllByCard(card);
//        List<CardImageSingleReadResponseDTO> cardImageSingleReadResponseDTOS = new ArrayList<>();
//
//        for (CardImage cardImage : cardImages) {
//            cardImageSingleReadResponseDTOS.add(CardImageSingleReadResponseDTO.from(cardImage.getImageUrl()));
//        }
//
//        return CardSingleReadResponseDTO.of(
//            cardInfo,
//            userinfo
//        );
//    }

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
                suggestionTargetCard.getCardId()
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

    @Transactional
    public List<SuggestionAvailableCardResponseDTO> getSuggestionResultCardList(
        Long targetId,
        List<SuggestionAvailableCardResponseDTO> cardList
    ) {
        Card targetCard = cardRepository.findById(targetId).orElseThrow();

        Boolean pokeAvailable = targetCard.getPoke();
        PriceRange priceRange = targetCard.getItem().getPriceRange();;

        if (pokeAvailable) {
            return cardList.stream()
                .peek(c -> {
                    if (c.getCardInfo().getPriceRange().getValue() < priceRange.getValue()) {
                        c.getSuggestionInfo().updateSuggestionType(SuggestionType.POKE);
                    } else {
                        c.getSuggestionInfo().updateSuggestionType(SuggestionType.OFFER);
                    }
                }).toList();
        }

        List<SuggestionAvailableCardResponseDTO> offerOnlyCardList = cardList.stream()
            .filter(c -> c.getCardInfo().getPriceRange().getValue() >= priceRange.getValue())
            .toList();
        offerOnlyCardList.forEach(o -> o.getSuggestionInfo().updateSuggestionType(SuggestionType.OFFER));

        return offerOnlyCardList;
    }
}
