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

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    public RefreshToken updateToken(String token) {
        this.token = token;

        return this;
    }

    public RefreshToken(
        Long userId,
        String token
    ) {
        this.userId = userId;
        this.token = token;
    }
}
