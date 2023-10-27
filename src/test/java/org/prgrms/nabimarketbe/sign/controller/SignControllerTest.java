//package org.prgrms.nabimarketbe.sign.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.prgrms.nabimarketbe.sign.dto.UserSignupRequestDto;
//import org.prgrms.nabimarketbe.user.entity.User;
//import org.prgrms.nabimarketbe.user.repository.UserJpaRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.env.Environment;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.prgrms.nabimarketbe.sign.dto.UserLoginRequestDto;
//import org.prgrms.nabimarketbe.sign.dto.UserSocialSignupRequestDto;
//import org.prgrms.nabimarketbe.sign.dto.UserSocialLoginRequestDto;
//
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Collections;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class SignControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserJpaRepo userJpaRepo;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    Environment env;
//
//    private static String accessToken;
//
//    @Before
//    public void setUp() {
//        userJpaRepo.save(User.builder()
//                .password(passwordEncoder.encode("password"))
//                .nickname("xinxinzara")
//                .roles(Collections.singletonList("ROLE_USER"))
//                .build());
//        accessToken = env.getProperty("social.kakao.accessToken");
//    }
//
//    @Test
//    public void 로그인_성공() throws Exception {
//        String object = objectMapper.writeValueAsString(UserLoginRequestDto.builder()
//                .nickname("xinxinzara")
//                .password("password")
//                .build());
//        //given
//        ResultActions actions = mockMvc.perform(post("/v1/login")
//                .content(object)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        //then
//        actions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").exists());
//    }
//
//    @Test
//    public void 로그인_실패() throws Exception {
//        //given
//        String object = objectMapper.writeValueAsString(UserLoginRequestDto.builder()
//                        .nickname("xinxinzara")
//                .password("wrongPassword")
//                .build());
//        //when
//        ResultActions actions = mockMvc.perform(
//                post("/v1/login")
//                        .content(object)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON));
//        //then
//        actions
//                .andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.code").value(-1001));
//    }
//
//    @Test
//    public void 회원가입_성공() throws Exception {
//        //given
//        long time = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
//
//        String object = objectMapper.writeValueAsString(UserSignupRequestDto.builder()
//                .nickname("woonsik")
//                .password("myPassword")
//                .build());
//        ResultActions actions = mockMvc.perform(
//                post("/v1/signup")
//                        .content(object)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON));
//
//        //then
//        actions.
//                andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.code").value(0))
//                .andExpect(jsonPath("$.msg").exists());
//    }
//
//    @Test
//    public void 회원가입_실패() throws Exception {
//        //given
//        String object = objectMapper.writeValueAsString(UserSignupRequestDto.builder()
//                .password("password")
//                .nickname("woonsik")
//                .build());
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                post("/v1/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(object));
//
//        //then
//        actions.andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.code").value(-1002));
//    }
//
//    @Test
//    public void 카카오_회원가입_성공() throws Exception {
//        //given
//        String object = objectMapper.writeValueAsString(UserSocialSignupRequestDto.builder()
//                .accessToken(accessToken)
//                .build());
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                post("/v1/social/signup/kakao")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(object)
//        );
//
//        //then
//        actions
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.code").value(0));
//    }
//
//    @Test
//    public void 카카오_회원가입_토큰에러_실패() throws Exception
//    {
//        //given
//        String object = objectMapper.writeValueAsString(UserSocialSignupRequestDto.builder()
//                .accessToken(accessToken+"_wrongToken")
//                .build());
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                post("/v1/social/signup/kakao")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(object));
//
//        //then
//        actions.andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.code").value("-1007"));
//    }
//
//    @Test
//    public void 카카오_회원가입_기가입_유저_실패() throws Exception
//    {
//        //given
//        userJpaRepo.save(User.builder()
//                .nickname("woonsik")
//                .provider("kakao")
//                .build());
//
//        String object = objectMapper.writeValueAsString(UserSocialSignupRequestDto.builder()
//                .accessToken(accessToken)
//                .build());
//
//        //when
//        ResultActions actions = mockMvc.perform(post("/v1/social/signup/kakao")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(object));
//
//        //then
//        actions
//                .andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.code").value("-1008"));
//    }
//
//    @Test
//    public void 카카오_로그인_성공() throws Exception
//    {
//        //given
//        String object = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
//                .accessToken(accessToken)
//                .build());
//
//        //when
//        mockMvc.perform(post("/v1/social/signup/kakao")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(object));
//
//        ResultActions actions = mockMvc.perform(post("/v1/social/login/kakao")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(object));
//
//        //then
//        actions
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.code").value(0));
//    }
//
//    @Test
//    public void 카카오_로그인_액세스토큰오류_실패() throws Exception
//    {
//        //given
//        String signUpObject = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
//                .accessToken(accessToken)
//                .build());
//        String logInObject = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
//                .accessToken(accessToken+"_wrongToken")
//                .build());
//
//        //when
//        mockMvc.perform(post("/v1/social/signup/kakao")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(signUpObject));
//
//        ResultActions actions = mockMvc.perform(post("/v1/social/login/kakao")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(logInObject));
//
//        //then
//        actions
//                .andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.code").value(-1007));
//    }
//
//    @Test
//    public void 카카오_로그인_비가입자_실패() throws Exception
//    {
//        //given
//        String logInObject = objectMapper.writeValueAsString(UserSocialLoginRequestDto.builder()
//                .accessToken(accessToken)
//                .build());
//
//        //when
//        ResultActions actions = mockMvc.perform(post("/v1/social/login/kakao")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(logInObject));
//
//        //then
//        actions
//                .andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.code").value(-1000));
//    }
//
//    @Test
//    @WithMockUser(username = "mockUser", roles = {"GUEST"})
//    public void 접근실패() throws Exception {
//        //then
//        mockMvc.perform(get("/v1/users"))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/exception/accessDenied"));
//        ;
//    }
//
//    @Test
//    @WithMockUser(username = "mockUser", roles = {"GUEST", "USER"})
//    public void 접근성공() throws Exception {
//        //then
//        mockMvc.perform(
//                        get("/v1/users"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//}