package org.prgrms.nabimarketbe.domain.cardimage.repository;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardImageBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String sql = "INSERT INTO card_images (created_date, modified_date, image_url, card_id) VALUES (?, ?, ?, ?)";

    private static final Integer SUCCESS_SIGNAL = -2;

    @Transactional
    public boolean saveAll(List<CardImage> cardImages) {
        int[][] result = jdbcTemplate.batchUpdate(sql,
            cardImages,
            cardImages.size(),
            (PreparedStatement preparedStatement, CardImage cardImage) -> {
                preparedStatement.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(3, cardImage.getImageUrl());
                preparedStatement.setLong(4,cardImage.getCard().getCardId());
            });

        for (int[] value : result) {
            if ( value[0] != SUCCESS_SIGNAL) {
                return false;
            }
        }

        return true;
    }
}
