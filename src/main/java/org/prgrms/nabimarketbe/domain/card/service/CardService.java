package org.prgrms.nabimarketbe.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.cardimage.dto.request.CardImageCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.cardimage.dto.response.CardImageSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.domain.category.entity.Category;
import org.prgrms.nabimarketbe.domain.category.repository.CategoryRepository;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.domain.item.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;

    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    private final CardImageRepository cardImageRepository;

    @Transactional
    public CardCreateResponseDTO createCard(CardCreateRequestDTO cardCreateRequestDTO) {  // TODO: 받은 image 버킷에 업로드
        Category category = categoryRepository.findCategoryByCategoryName(cardCreateRequestDTO.category())
                .orElseThrow();

        Item item = cardCreateRequestDTO.toItemEntity(category);

        Card card = cardCreateRequestDTO.toCardEntity(item);

        List<CardImage> cardImages = new ArrayList<>(cardCreateRequestDTO.images().stream()
                .sorted(Comparator.comparingInt(CardImageCreateRequestDTO::_id))
                .map(cardImageCreateRequestDTO -> CardImage.builder()
                        .imageUrl(cardImageCreateRequestDTO.binary())
                        .card(card)
                        .build())
                .toList());
        cardImages.add(0, CardImage.builder()
                .imageUrl(cardCreateRequestDTO.thumbNailImage())
                .card(card)
                .build());

        Item savedItem = itemRepository.save(item);
        Card savedCard =  cardRepository.save(card);
        List<CardImage> savedCardImages = cardImageRepository.saveAll(cardImages);    // TODO: bulk insert 로 전환

        // image 순서 맞춰서 응답 내려주는 부분
        int idx = 0;
        List<CardImageCreateResponseDTO> cardImageCreateResponseDTOS = new ArrayList<>();
        for (CardImage cardImage : savedCardImages) {
            cardImageCreateResponseDTOS.add(CardImageCreateResponseDTO.of(idx++, cardImage.getImageUrl()));
        }

        return CardCreateResponseDTO.of(
                savedCard.getCardId(),
                savedCard.getCardTitle(),
                savedCard.getThumbNailImage(),
                savedItem.getItemName(),
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

    @Transactional(readOnly = true)
    public CardSingleReadResponseDTO getCardById(Long cardId) {  // TODO: User 완성되면 User에 대한 정보도 추가해서 내려주기
        Card card = cardRepository.findById(cardId)
                .orElseThrow();

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

    @Transactional
    public void updateViews(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("해당 카드가 존재하지 않습니다."));

        card.updateViewCount();
    }
}
