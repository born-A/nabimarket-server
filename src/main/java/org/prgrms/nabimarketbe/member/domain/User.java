package org.prgrms.nabimarketbe.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(name = "user_name")
	private String name;

	@Column(name = "user_picture_source")
	private String picture;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private Role role;

	@Builder
	public User(String email, String name, String picture, Role role){
		this.email=email;
		this.name=name;
		this.picture=picture;
		this.role=role;
	}

	public User update(String name, String picture){
		this.name=name;
		this.picture=picture;
		return this;
	}

	public String getRoleKey(){
		return role.getKey();
	}


}
