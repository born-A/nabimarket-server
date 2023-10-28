package org.prgrms.nabimarketbe.user.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.user.repository.UserJpaRepo;
import org.prgrms.nabimarketbe.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "mockUser")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserJpaRepo userJpaRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static int id;

    @Before
    public void setUp() {
        User save = userJpaRepo.save(User.builder()
                .nickname("xinxinzara")
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        id = Math.toIntExact(save.getUserId());
    }

    @Test
    public void 회원조회_닉네임() throws Exception {
        //then
        ResultActions actions = mockMvc.perform(
                get("/v1/user/nickname/{nickname}", "xinxinzara")
                        .param("lang", "en"));

        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickName", is("xinxinzara")))
                .andReturn();
    }

    @Test
    public void 회원조회_userId() throws Exception {
        //given
        ResultActions actions = mockMvc.perform(get("/v1/user/id/{id}", id)
                .param("lang", "en"));
        //when
        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId", is(id)))
                .andExpect(jsonPath("$.data.nickName", is("xinxinzara")));
    }

    @Test
    public void 전체_회원조회() throws Exception {
        //then
        mockMvc.perform(get("/v1/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원수정() throws Exception {
        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", String.valueOf(id));
        params.add("nickName", "afterNickName");
        //when
        ResultActions actions = mockMvc.perform(put("/v1/user")
                .params(params));
        //then
        actions
                .andDo(print())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data", is(id)));
    }

    @Test
    public void 회원삭제() throws Exception {
        //given
        //when
        ResultActions actions = mockMvc.perform(delete("/v1/user/{id}", id));
        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.msg").exists());
    }
}