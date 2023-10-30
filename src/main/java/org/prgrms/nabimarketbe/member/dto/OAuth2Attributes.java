package org.prgrms.nabimarketbe.member.dto;

import java.util.Map;

import org.prgrms.nabimarketbe.member.domain.Role;
import org.prgrms.nabimarketbe.member.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {

	public Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String picture;

	@Builder
	public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email
		,String picture){
		this.attributes=attributes;
		this.nameAttributeKey=nameAttributeKey;
		this.name=name;
		this.email=email;
		this.picture=picture;
	}

	public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes){
		return ofGoogle(userNameAttributeName, attributes);
	}

	private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String,Object> attributes){
		return OAuth2Attributes.builder()
			.name((String) attributes.get("name"))
			.email((String)attributes.get("email"))
			.nameAttributeKey(userNameAttributeName)
			.attributes(attributes)
			.picture((String) attributes.get("picture"))
			.build();
	}

	public User toEntity(){
		return User.builder()
			.email(email)
			.nickname(name)
			.picture(picture)
			.role(Role.GUEST)
			.build();
	}

}
