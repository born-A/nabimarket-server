package org.prgrms.nabimarketbe.domain.cardimage.service;

import java.util.ArrayList;
import java.util.List;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardImageService {
    private final CardImageRepository cardImageRepository;

    private final CardRepository cardRepository;

    private final S3FileUploadService s3FileUploadService;

    @Transactional
    public String uploadImageUrl(Long cardId, String imageUrl) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (card.getThumbnail() != null) {
            String thumbnail = card.getThumbnail();
            s3FileUploadService.deleteImage(thumbnail);
        }

        card.updateThumbnail(imageUrl);

        CardImage cardImage = CardImage.builder()
                .imageUrl(imageUrl)
                .card(card)
                .build();

        cardImageRepository.save(cardImage);

        return imageUrl;
    }

    @Transactional
    public List<CardImage> uploadImageUrlList(Long cardId, List<String> imageUrls) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        List<CardImage> cardImageList = new ArrayList<>();

        for (String url : imageUrls) {
            CardImage cardImage = CardImage.builder()
                    .imageUrl(url)
                    .card(card)
                    .build();

            cardImageRepository.save(cardImage);

            cardImageList.add(cardImage);
        }

        return cardImageList;
    }
}
