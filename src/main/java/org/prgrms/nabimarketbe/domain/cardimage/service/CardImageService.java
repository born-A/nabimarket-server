package org.prgrms.nabimarketbe.domain.cardimage.service;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.prgrms.nabimarketbe.domain.cardimage.repository.CardImageRepository;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardImageService {
    private final CardImageRepository cardImageRepository;

    private final CardRepository cardRepository;

    private final S3FileUploadService s3FileUploadService;

    @Transactional
    public String uploadImageUrl(Long cardId, MultipartFile file) throws IOException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("해당 카드가 존재하지 않습니다"));

        String url = s3FileUploadService.uploadFile(file);

        CardImage cardImage = CardImage.builder()
                .imageUrl(url)
                .card(card)
                .build();

        cardImageRepository.save(cardImage);
        return url;
    }

    @Transactional
    public List<CardImage> uploadImageUrlList(Long cardId, List<MultipartFile> files) throws IOException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("해당 카드가 존재하지 않습니다"));

        List<String> uploadFileList = null;

        uploadFileList = s3FileUploadService.uploadFileList(files);

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
