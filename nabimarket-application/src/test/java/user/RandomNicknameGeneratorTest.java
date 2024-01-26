package user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.prgrms.nabimarketbe.user.service.RandomNicknameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@Import({RandomNicknameGenerator.class, ObjectMapper.class})
@EnableRetry
class RandomNicknameGeneratorTest {
    @Autowired
    RandomNicknameGenerator randomNicknameGenerator;

    @MockBean
    RestTemplate restTemplate;

    @DisplayName("랜덤닉네임 생성 시에 RuntimeException 발생시 2번까지 재시도한다.")
    @Test
    void generateRandomNickname_with_retry() {
        // given
        doThrow(new RuntimeException("Intended Retry Error")).when(restTemplate)
            .getForObject(anyString(), eq(String.class));

        // then
        assertThrows(RuntimeException.class, () -> {
            randomNicknameGenerator.generateRandomNickname();
        });
        verify(restTemplate, times(2)).getForObject(anyString(), eq(String.class));
    }
}
