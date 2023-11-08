package org.prgrms.nabimarketbe.setup.domain.user;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSetUp {

	final UserRepository userRepository;

	public User saveOne() {
		User user = User.builder()
			.nickname("테스트닉네임")
			.build();

		User savedUser = userRepository.save(user);
		return savedUser;
	}
}
