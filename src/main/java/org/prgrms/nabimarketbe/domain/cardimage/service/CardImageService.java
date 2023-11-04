package org.prgrms.nabimarketbe.domain.cardimage.service;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.global.Domain;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardImageService {
    private final CardImageRepository cardImageRepository;

    private final CardRepository cardRepository;

    private final S3FileUploadService s3FileUploadService;

    @Transactional
    public String uploadImageUrl(Long cardId, MultipartFile file)  {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("해당 카드가 존재하지 않습니다"));

        if (card.getThumbNailImage() != null) {
            String thumbNailImage = card.getThumbNailImage();
            s3FileUploadService.deleteImage(thumbNailImage);
        }

        String url = s3FileUploadService.uploadFile(Domain.CARD.name(), cardId, file);

        CardImage cardImage = CardImage.builder()
                .imageUrl(url)
                .card(card)
                .build();

        cardImageRepository.save(cardImage);

        return url;
    }

    @Transactional
    public List<CardImage> uploadImageUrlList(Long cardId, List<MultipartFile> files) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("해당 카드가 존재하지 않습니다"));

        List<String> uploadFileList =  s3FileUploadService.uploadFileList(Domain.CARD.name(), cardId, files);

        List<CardImage> cardImageList = new ArrayList<>();

        for (String url : uploadFileList) {
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
