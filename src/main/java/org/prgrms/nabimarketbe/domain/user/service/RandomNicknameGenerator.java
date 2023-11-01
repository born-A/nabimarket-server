package org.prgrms.nabimarketbe.domain.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RandomNicknameGenerator {
	private static final String GENERATOR_URL = "https://nickname.hwanmoo.kr/?format=json&count=1";

	private static final int NICKNAME_MAX_LENGTH = 6;

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	public String generateRandomNickname() throws JsonProcessingException {
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(GENERATOR_URL)
			.queryParam("max_length", NICKNAME_MAX_LENGTH).build();

		String apiResult = restTemplate.getForObject(uriComponents.toString(), String.class);
		Map<String, Object> wordMap = objectMapper.readValue(apiResult, Map.class);

		List<String> randomNicknames = (List<String>) wordMap.get("words");
		String randomNickname = randomNicknames.get(0);

		return randomNickname;
	}
}
