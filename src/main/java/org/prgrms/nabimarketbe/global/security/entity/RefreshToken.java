package org.prgrms.nabimarketbe.global.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_id", nullable = false)
    private Long key;

    @Column(nullable = false)
    private String token;

    public RefreshToken updateToken(String token) {
        this.token = token;

        return this;
    }

    public RefreshToken(
            Long key,
            String token
    ) {
        this.key = key;
        this.token = token;
    }
}
