package org.prgrms.nabimarketbe.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_email")
	private String email;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "user_picture_source")
	private String picture;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private Role role;

	private String refreshToken; // 리프레시 토큰

	@Builder
	public User(String email, String nickname, String picture, Role role){
		this.email = email;
		this.nickname = nickname;
		this.picture = picture;
		this.role = role;
	}

	public User update(String nickname, String picture){
		this.nickname = nickname;
		this.picture = picture;
		return this;
	}

	public String getRoleKey(){
		return role.getKey();
	}

	public void authorizeUser() {
		this.role = Role.USER;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
