package org.prgrms.nabimarketbe.oauth2.google.member.oauth2.service;

import java.util.Collections;
import java.util.Optional;

import javax.transaction.Transactional;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserJpaRepo;
import org.prgrms.nabimarketbe.oauth2.google.member.dto.OAuth2Attributes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserJpaRepo userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		log.info("loadUser : {} ", userRequest.toString());

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
			.getUserNameAttributeName();

		OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

		User user = saveIfNotPresent(attributes);

		return new DefaultOAuth2User(Collections.singleton(
			new SimpleGrantedAuthority(user.getRoles().get(0))),
			attributes.getAttributes(),
			attributes.getNameAttributeKey()
		);
	}

	// TODO : 메소드 기능에 맞게 수정하기
	private User saveIfNotPresent(OAuth2Attributes attributes){
		Optional<User> user = userRepository.findByEmail(attributes.getEmail());
		if(user.isEmpty()) {
			return userRepository.save(attributes.toEntity());
		}

		return user.get();
	}
}
