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
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageBatchRepository;
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

        CardImage thumbnail = new CardImage(cardCreateRequestDTO.thumbnail(), card);

        List<CardImage> images = Optional.ofNullable(cardCreateRequestDTO.images())
            .orElse(new ArrayList<>())
            .stream()
            .map(i -> i.toCardImageEntity(card))
            .toList();

        List<CardImage> newCardImages = addThumbnail(images, thumbnail);

        Item savedItem = itemRepository.save(item);
        Card savedCard = cardRepository.save(card);

        if (!cardImageBatchRepository.saveAll(newCardImages)) {
            throw new BaseException(ErrorCode.BATCH_INSERT_ERROR);
        }

        CardCreateResponseDTO cardCreateResponseDTO = CardCreateResponseDTO.of(
            savedCard,
            savedItem,
            newCardImages
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
            cardUpdateRequestDTO.thumbnail(),
            cardUpdateRequestDTO.pokeAvailable(),
            cardUpdateRequestDTO.content(),
            cardUpdateRequestDTO.tradeType(),
            cardUpdateRequestDTO.tradeArea()
        );

        cardImageRepository.deleteAllByCard(card);

        CardImage thumbnail = new CardImage(cardUpdateRequestDTO.thumbnail(), card);

        List<CardImage> images = Optional.ofNullable(cardUpdateRequestDTO.images())
            .orElse(new ArrayList<>())
            .stream()
            .map(i -> i.toCardImageEntity(card))
            .toList();

        List<CardImage> newCardImages = addThumbnail(images, thumbnail);

        cardImageBatchRepository.saveAll(newCardImages);

        CardUpdateResponseDTO cardUpdateResponseDTO = CardUpdateResponseDTO.of(
            card,
            item,
            images
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
            Long targetCardId
    ) {
        User requestUser = userRepository.findById(checkService.parseToken(token))
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        Card suggestionTargetCard = cardRepository.findById(targetCardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (requestUser.getUserId().equals(suggestionTargetCard.getUser().getUserId())) {
            throw new BaseException(ErrorCode.CARD_SUGGESTION_MYSELF_ERROR);
        }

        List<SuggestionAvailableCardResponseDTO> cardListResponse = cardRepository.getSuggestionAvailableCards(
                requestUser.getUserId(),
                suggestionTargetCard.getCardId()
        );

        List<SuggestionAvailableCardResponseDTO> suggestionResultCardList =
            getSuggestionResultCardList(suggestionTargetCard.getCardId(), cardListResponse);

        return new CardListResponseDTO<>(suggestionResultCardList);
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

        cardImageRepository.deleteAllByCard(card);
        cardRepository.delete(card);
    }
  
    @Transactional
    public List<SuggestionAvailableCardResponseDTO> getSuggestionResultCardList(
        Long targetId,
        List<SuggestionAvailableCardResponseDTO> cardList
    ) {
        Card targetCard = cardRepository.findById(targetId).orElseThrow();

        Boolean pokeAvailable = targetCard.getPokeAvailable();
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
  
    private List<CardImage> addThumbnail(
      List<CardImage> cardImages,
      CardImage thumbnail
    ) {
        List<CardImage> newCardImages = new ArrayList<>(cardImages);
        newCardImages.add(0, thumbnail);

        return newCardImages;
    }
}
