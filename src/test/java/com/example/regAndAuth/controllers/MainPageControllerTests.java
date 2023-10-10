package com.example.regAndAuth.controllers;

import com.example.regAndAuth.dtos.ProfilePayload;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainPageControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    MainPageControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    public void mainPageAccessFailTest() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @Order(2)
    @WithUserDetails("testing@gmail.com")
    public void authenticaredAccessToMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//header[contains(text(), 'Tester')]").exists())
                .andExpect(xpath("//input[@name='name']/@value").string("Tester"))
                .andExpect(xpath("//input[@name='email']/@value").string("testing@gmail.com"));
    }

    @Test
    @Order(3)
    @WithUserDetails("testing@gmail.com")
    public void changeProfile() throws Exception {
        ProfilePayload profilePayload = new ProfilePayload();
        profilePayload.setEmail("testing2@gmail.com");
        profilePayload.setName("Tester2");
        profilePayload.setPassword("222");

        mockMvc.perform(post("/changeProfile")
                        .flashAttr("profilePayload", profilePayload)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("changeDataResult"))
                .andDo(result ->
                        mockMvc.perform(get("/"))
                                .andDo(print())
                                .andExpect(xpath("//header[contains(text(), 'Tester2')]").exists())
                                .andExpect(xpath("//input[@name='name']/@value").string("Tester2"))
                                .andExpect(xpath("//input[@name='email']/@value").string("testing2@gmail.com")));
    }
}