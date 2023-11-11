package org.prgrms.nabimarketbe.domain.card.service;

import java.util.ArrayList;
import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionAvailableCardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.domain.cardimage.service.CardImageService;
import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.category.repository.CategoryRepository;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.item.repository.ItemRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    private final CardImageRepository cardImageRepository;

    private final UserRepository userRepository;

    private final CardImageService cardImageService;

    private final CheckService checkService;

    @Transactional
    public CardCreateResponseDTO createCard(
            String token,
            CardCreateRequestDTO cardCreateRequestDTO,
            MultipartFile thumbnail,
            List<MultipartFile> imageFiles
    ) {
        User user = userRepository.findById(checkService.parseToken(token))
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findCategoryByCategoryName(cardCreateRequestDTO.category())
                .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));

        Item item = cardCreateRequestDTO.toItemEntity(category);
        Card card = cardCreateRequestDTO.toCardEntity(item, user);

        Item savedItem = itemRepository.save(item);
        Card savedCard =  cardRepository.save(card);

        String thumbnailUrl = cardImageService.uploadImageUrl(card.getCardId(), thumbnail);

        savedCard.updateThumbNailImage(thumbnailUrl);

        // TODO: bulk insert 로 전환
        List<CardImage> savedCardImages = cardImageService.uploadImageUrlList(card.getCardId(),imageFiles);

        savedCardImages.add(0, CardImage.builder()
                .imageUrl(thumbnailUrl)
                .card(card)
                .build());

        // image 순서 맞춰서 응답 내려주는 부분
        int idx = 0;

        List<CardImageCreateResponseDTO> cardImageCreateResponseDTOS = new ArrayList<>();

        for (CardImage cardImage : savedCardImages) {
            cardImageCreateResponseDTOS.add(CardImageCreateResponseDTO.of(idx++, cardImage.getImageUrl()));
        }

        return CardCreateResponseDTO.of(
                savedCard.getCardId(),
                savedCard.getCardTitle(),
                savedItem.getItemName(),
                savedCard.getThumbNailImage(),
                savedItem.getPriceRange(),
                savedCard.getTradeType(),
                savedItem.getCategory().getCategoryName(),
                savedCard.getTradeArea(),
                savedCard.getPoke(),
                savedCard.getContent(),
                savedCard.getViewCount(),
                savedCard.getDibCount(),
                savedCard.getCreatedDate(),
                savedCard.getModifiedDate(),
                cardImageCreateResponseDTOS
        );
    }

    @Transactional
    public CardSingleReadResponseDTO getCardById(Long cardId) {  // TODO: User 완성되면 User에 대한 정보도 추가해서 내려주기
        Card card = cardRepository.findById(cardId)
                .orElseThrow();

        card.updateViewCount();

        Item item = card.getItem();

        // image 순서 맞춰서 응답 내려주는 부분
        int idx = 0;

        List<CardImage> cardImages = cardImageRepository.findAllByCard(card);
        List<CardImageSingleReadResponseDTO> cardImageSingleReadResponseDTOS = new ArrayList<>();

        for (CardImage cardImage : cardImages) {
            cardImageSingleReadResponseDTOS.add(CardImageSingleReadResponseDTO.of(idx++, cardImage.getImageUrl()));
        }

        return CardSingleReadResponseDTO.of(
                card.getCardId(),
                card.getCardTitle(),
                card.getContent(),
                card.getTradeArea(),
                card.getPoke(),
                card.getTradeType(),
                card.getStatus(),
                card.getViewCount(),
                card.getDibCount(),
                item.getItemName(),
                item.getCategory().getCategoryName(),
                item.getPriceRange(),
                cardImageSingleReadResponseDTOS
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
                suggestionTargetCard.getPoke()
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
}
