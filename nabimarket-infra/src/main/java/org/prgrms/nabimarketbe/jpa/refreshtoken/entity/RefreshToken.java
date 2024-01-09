package org.prgrms.nabimarketbe.jpa.refreshtoken.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.prgrms.nabimarketbe.jpa.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
