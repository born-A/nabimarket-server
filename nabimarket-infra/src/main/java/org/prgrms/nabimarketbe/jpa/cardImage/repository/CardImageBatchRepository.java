package org.prgrms.nabimarketbe.jpa.cardImage.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.prgrms.nabimarketbe.jpa.cardImage.entity.CardImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardImageBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String sql = "INSERT INTO card_images (created_date, modified_date, image_url, card_id) VALUES (?, ?, ?, ?)";

    @Transactional
    public void saveAll(List<CardImage> cardImages) {
        jdbcTemplate.batchUpdate(
            sql,
            cardImages,
            cardImages.size(),
            (PreparedStatement preparedStatement, CardImage cardImage) -> {
                preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(3, cardImage.getImageUrl());
                preparedStatement.setLong(4, cardImage.getCard().getCardId());
            });
    }
}
