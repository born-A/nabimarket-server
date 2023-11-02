//package org.prgrms.nabimarketbe.oauth2.google.member.dto;
//
//import java.util.Collections;
//import java.util.Map;
//
//import org.prgrms.nabimarketbe.domain.user.entity.User;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//public class OAuth2Attributes {
//
//	public Map<String, Object> attributes;
//	private String nameAttributeKey;
//	private String name;
//	private String email;
//	private String image_url;
//
//	@Builder
//	public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email
//		,String image_url){
//		this.attributes=attributes;
//		this.nameAttributeKey=nameAttributeKey;
//		this.name=name;
//		this.email=email;
//		this.image_url = image_url;
//	}
//
//	public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes){
//		return ofGoogle(userNameAttributeName, attributes);
//	}
//
//	private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String,Object> attributes){
//		return OAuth2Attributes.builder()
//			.name((String) attributes.get("name"))
//			.email((String)attributes.get("email"))
//			.nameAttributeKey(userNameAttributeName)
//			.attributes(attributes)
//			.image_url((String) attributes.get("picture"))
//			.build();
//	}
//
//	public User toEntity(){
//		return User.builder()
//			.email(email)
//			.nickname(name)
//			.image_url(image_url)
//			.roles(Collections.singletonList("ROLE_USER"))
//			.build();
//	}
//
//}
