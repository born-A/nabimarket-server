package org.prgrms.nabimarketbe.config.security.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.user.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private Long key;

    @Column(nullable = false)
    private String token;

    public RefreshToken updateToken(String token) {
        this.token = token;

        return this;
    }

    @Builder
    public RefreshToken(
            Long key,
            String token
    ) {
        this.key = key;
        this.token = token;
    }
}
