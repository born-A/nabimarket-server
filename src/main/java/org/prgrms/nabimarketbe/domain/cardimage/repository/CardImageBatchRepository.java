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

    @Transactional
    public boolean saveAll(List<CardImage> cardImages) {
        String sql = "INSERT INTO card_images (created_date, modified_date, image_url, card_id) VALUES (?, ?, ?, ?)";

        int[][] result = jdbcTemplate.batchUpdate(sql,
            cardImages,
            cardImages.size(),
            (PreparedStatement ps, CardImage cardImage) -> {
                ps.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, cardImage.getImageUrl());
                ps.setLong(4,cardImage.getCard().getCardId());
            });

        // 각 배열의 값이 모두 -2이면 성공
        for (int[] r : result) {
            if ( r[0] != -2) {
                return false;
            }
        }
        return true;
    }
}
