package org.prgrms.nabimarketbe.domain.card.service;

import java.util.ArrayList;
import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardStatusUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardDetailResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardFamousResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardListResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardSuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardUserResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageBatchRepository;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.category.repository.CategoryRepository;
import org.prgrms.nabimarketbe.domain.dibs.repository.DibRepository;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.item.repository.ItemRepository;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.domain.suggestion.repository.SuggestionRepository;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.prgrms.nabimarketbe.global.util.OrderCondition;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final SuggestionRepository suggestionRepository;

    private final UserRepository userRepository;

    private final CheckService checkService;

    private final DibRepository dibRepository;

    private final CardImageBatchRepository cardImageBatchRepository;

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

        List<CardImage> images = cardCreateRequestDTO.images()
            .stream()
            .map(i -> i.toCardImageEntity(card))
            .toList();

        Item savedItem = itemRepository.save(item);
        Card savedCard = cardRepository.save(card);

        cardImageBatchRepository.saveAll(images);

        CardCreateResponseDTO cardCreateResponseDTO = CardCreateResponseDTO.of(
            savedCard,
            savedItem,
            images
        );

        return new CardResponseDTO<>(cardCreateResponseDTO);
    }

    @Transactional
    public CardResponseDTO<CardUpdateResponseDTO> updateCardById(
        String token,
        Long cardId,
        CardUpdateRequestDTO cardUpdateRequestDTO
    ) {
        Long userId = checkService.parseToken(token);
        if (!userRepository.existsById(userId)) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_MATCHED));

        if (!checkService.isEqual(userId, card.getUser().getUserId())) {
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
            cardUpdateRequestDTO.thumbnail(),
            cardUpdateRequestDTO.pokeAvailable(),
            cardUpdateRequestDTO.content(),
            cardUpdateRequestDTO.tradeType(),
            cardUpdateRequestDTO.tradeArea()
        );

        cardImageRepository.deleteAllByCard(card);

        List<CardImage> images = cardUpdateRequestDTO.images()
            .stream()
            .map(i -> i.toCardImageEntity(card))
            .toList();

        cardImageBatchRepository.saveAll(images);

        CardUpdateResponseDTO cardUpdateResponseDTO = CardUpdateResponseDTO.of(
            card,
            item,
            images
        );

        return new CardResponseDTO<>(cardUpdateResponseDTO);
    }

    @Transactional
    public CardUserResponseDTO getCardById(
        String token,
        Long cardId
    ) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Boolean isMyDib = false;

        if (token != null) {
            Long userId = checkService.parseToken(token);
            User loginUser = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

            if (!checkService.isEqual(userId, card.getUser().getUserId())) {
                card.updateViewCount();
                isMyDib = dibRepository.existsDibByCardAndUser(card, loginUser);
            }
        }

        User cardOwner = card.getUser();

        List<CardImage> cardImages = cardImageRepository.findAllByCard(card);

        CardDetailResponseDTO cardInfo = CardDetailResponseDTO.of(
            card,
            cardImages,
            isMyDib
        );

        UserSummaryResponseDTO userInfo = UserSummaryResponseDTO.from(cardOwner);

        return CardUserResponseDTO.of(
            cardInfo,
            userInfo
        );
    }

    @Transactional(readOnly = true)
    public CardPagingResponseDTO getCardsByCondition(
            CategoryEnum category,
            PriceRange priceRange,
            List<CardStatus> status,
            String title,
            String cursorId,
            Integer size,
            OrderCondition orderCondition
    ) {

        // 전달 받은 orderCondition 과 page size 에 맞게 pageRequest 를 구성 후 repo 에 넘김
        PageRequest pageRequest = PageRequest.of(
                0,
                size,
                getSortFromOrderCondition(orderCondition)
        );

        return cardRepository.getCardsByCondition(
            category,
            priceRange,
            status,
            title,
            cursorId,
            pageRequest
        );
    }

    @Transactional(readOnly = true)
    public CardListResponseDTO<CardSuggestionResponseDTO> getSuggestionAvailableCards(
        String token,
        Long targetCardId
    ) {
        Long userId = checkService.parseToken(token);
        User requestUser = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card suggestionTargetCard = cardRepository.findById(targetCardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (checkService.isEqual(userId, suggestionTargetCard.getUser().getUserId())) {
            throw new BaseException(ErrorCode.CARD_SUGGESTION_MYSELF_ERROR);
        }

        List<CardSuggestionResponseDTO> cardListResponse = cardRepository.getSuggestionAvailableCards(
            requestUser.getUserId(),
            suggestionTargetCard.getCardId()
        );

        List<CardSuggestionResponseDTO> suggestionResultCardList =
            getSuggestionResultCardList(suggestionTargetCard.getCardId(), cardListResponse);

        List<CardSuggestionResponseDTO> responseDTOList = getResponseDTOList(
            suggestionResultCardList,
            suggestionTargetCard
        );

        return new CardListResponseDTO<>(responseDTOList);
    }

    private List<CardSuggestionResponseDTO> getResponseDTOList(
        List<CardSuggestionResponseDTO> suggestionResultCardList,
        Card suggestionTargetCard
    ) {
        List<CardSuggestionResponseDTO> cardsToRemove = new ArrayList<>();

        for (CardSuggestionResponseDTO responseDTO : suggestionResultCardList) {
            Long cardId = responseDTO.getCardInfo().getCardId();
            Card fromCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

            if (fromCard.getStatus() != CardStatus.TRADE_AVAILABLE ||
                suggestionRepository.exists(fromCard, suggestionTargetCard)) {
                cardsToRemove.add(responseDTO);
            }
        }

        List<CardSuggestionResponseDTO> modifiableList = new ArrayList<>(suggestionResultCardList);
        modifiableList.removeAll(cardsToRemove);
        return modifiableList;
    }

    @Transactional(readOnly = true)
    public CardPagingResponseDTO getMyCardsByStatus(
        String token,
        CardStatus status,
        String cursorId,
        Integer size
    ) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
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
        Long userId = checkService.parseToken(token);
        if (!userRepository.existsById(userId)) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (!checkService.isEqual(userId, card.getUser().getUserId())) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }

        switch (cardStatusUpdateRequestDTO.status()) {
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
        Long userId = checkService.parseToken(token);
        if (!userRepository.existsById(userId)) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (!checkService.isEqual(userId, card.getUser().getUserId())) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }

        card.deleteCard();
    }

    @Transactional
    public List<CardSuggestionResponseDTO> getSuggestionResultCardList(
        Long targetId,
        List<CardSuggestionResponseDTO> cardList
    ) {
        Card targetCard = cardRepository.findById(targetId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Boolean pokeAvailable = targetCard.getPokeAvailable();
        PriceRange priceRange = targetCard.getItem().getPriceRange();

        if (pokeAvailable) {
            return parseCardListWithPokeAndOffer(cardList, priceRange);
        }

        return parseCardListWithOnlyOffer(cardList, priceRange);
    }

    @Transactional(readOnly = true)
    public CardListResponseDTO<CardFamousResponseDTO> getCardsByPopularity() {
        List<CardFamousResponseDTO> cardList = cardRepository.getCardsByPopularity();
        CardListResponseDTO<CardFamousResponseDTO> response = new CardListResponseDTO<>(cardList);

        return response;
    }

    private List<CardSuggestionResponseDTO> parseCardListWithPokeAndOffer(
        List<CardSuggestionResponseDTO> cardList,
        PriceRange targetPriceRange
    ) {
        return cardList.stream()
            .peek(cardSuggestionResponseDTO -> {
                if (targetPriceRange.isHigherThan(
                    cardSuggestionResponseDTO
                        .getCardInfo()
                        .getPriceRange()
                    )
                ) {
                    cardSuggestionResponseDTO
                        .getSuggestionInfo()
                        .updateSuggestionType(SuggestionType.POKE);
                } else {
                    cardSuggestionResponseDTO
                        .getSuggestionInfo()
                        .updateSuggestionType(SuggestionType.OFFER);
                }
            }).toList();
    }

    private List<CardSuggestionResponseDTO> parseCardListWithOnlyOffer(
        List<CardSuggestionResponseDTO> cardList,
        PriceRange priceRange
    ) {
        List<CardSuggestionResponseDTO> offerOnlyCardList = cardList.stream()
            .filter(
                cardSuggestionResponseDTO -> cardSuggestionResponseDTO
                    .getCardInfo()
                    .getPriceRange()
                    .isHigherThan(priceRange)
            )
            .toList();

        offerOnlyCardList.forEach(offerCard ->
            offerCard
                .getSuggestionInfo()
                .updateSuggestionType(SuggestionType.OFFER));

        return offerOnlyCardList;
    }

    private List<CardImage> addThumbnail(
        List<CardImage> cardImages,
        CardImage thumbnail
    ) {
        List<CardImage> newCardImages = new ArrayList<>(cardImages);
        newCardImages.add(0, thumbnail);

        return newCardImages;
    }

    private Sort getSortFromOrderCondition(OrderCondition orderCondition) {
        switch (orderCondition) {
            case CARD_CREATED_DESC -> {
                return Sort.by(
                        Sort.Order.desc("createdDate"),
                        Sort.Order.desc("cardId")
                );
            }
            default -> throw new BaseException(ErrorCode.INVALID_ORDER_CONDITION);
        }
    }
}
