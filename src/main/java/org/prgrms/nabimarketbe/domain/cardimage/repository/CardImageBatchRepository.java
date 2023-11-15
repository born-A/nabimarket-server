package org.prgrms.nabimarketbe.domain.cardimage.repository;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardImageBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<CardImage> cardImages) {
        String sql = "INSERT INTO card_images (created_date, modified_date, image_url, card_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
            cardImages,
            cardImages.size(),
            (PreparedStatement ps, CardImage cardImage) -> {
                ps.setTimestamp(1, cardImage.getCreatedDate() != null ? Timestamp.valueOf(cardImage.getCreatedDate()) : null);
                ps.setTimestamp(2, cardImage.getModifiedDate() != null ? Timestamp.valueOf(cardImage.getModifiedDate()) : null);
                ps.setString(3, cardImage.getImageUrl());
                ps.setLong(4,cardImage.getCard().getCardId());
            });
    }
}
