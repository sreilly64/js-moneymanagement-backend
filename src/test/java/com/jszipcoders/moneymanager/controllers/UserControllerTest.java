package com.jszipcoders.moneymanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void addUser() throws Exception{
        UserEntity user = new UserEntity(1L, "Shane", "Reilly", "123-45-6789", "4 Street Road", "email@gmail.com", "867-5309", "CodingRulez", "secretPassword");

        when(userService.addUser(user)).thenReturn(user);

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).addUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void addUserFail() throws Exception {
        UserEntity user = new UserEntity(1L, "Shane", "Reilly", "123-45-6789", "4 Street Road", "email@gmail.com", "867-5309", "CodingRulez", "secretPassword");

        when(userService.addUser(user)).thenThrow(new IllegalArgumentException("Some user data already in use"));

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).addUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void findUserById() throws Exception {
        UserEntity user = new UserEntity(1L, "Shane", "Reilly", "123-45-6789", "4 Street Road", "email@gmail.com", "867-5309", "CodingRulez", "secretPassword");

        when(userService.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/{user_id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.firstName", is("Shane")))
                .andExpect(jsonPath("$.lastName", is("Reilly")))
                .andExpect(jsonPath("$.ssn", is("123-45-6789")))
                .andExpect(jsonPath("$.address", is("4 Street Road")))
                .andExpect(jsonPath("$.email", is("email@gmail.com")))
                .andExpect(jsonPath("$.phoneNumber", is("867-5309")))
                .andExpect(jsonPath("$.username", is("CodingRulez")))
                .andExpect(jsonPath("$.password", is("secretPassword")));

        verify(userService, times(1)).findUserById(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void findUserByIdFail() throws Exception {
        when(userService.findUserById(1L)).thenThrow(new NoSuchElementException("no such value exists"));

        mockMvc.perform(get("/api/users/{user_id}", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUserById(1L);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateUserDetails() throws Exception {
        UserEntity user = new UserEntity(1L, "Shane", "Reilly", "123-45-6789", "4 Street Road", "email@gmail.com", "867-5309", "CodingRulez", "secretPassword");

        when(userService.updateUserDetails(1L,user)).thenReturn(user);

        mockMvc.perform(
                put("/api/users/{user_id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserDetails(1L,user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateUserDetailsFail() throws Exception {
        UserEntity user = new UserEntity(1L, "Shane", "Reilly", "123-45-6789", "4 Street Road", "email@gmail.com", "867-5309", "CodingRulez", "secretPassword");

        when(userService.updateUserDetails(1L,user)).thenThrow(new NoSuchElementException("no such user"));

        mockMvc.perform(
                put("/api/users/{user_id}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUserDetails(1L,user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserById() throws Exception {
        UserEntity user = new UserEntity(1L, "Shane", "Reilly", "123-45-6789", "4 Street Road", "email@gmail.com", "867-5309", "CodingRulez", "secretPassword");

        when(userService.deleteUserById(1L)).thenReturn(true);

        mockMvc.perform(
                delete("/api/users/{user_id}", user.getUserId()))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(user.getUserId());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserByIdFail() throws Exception {
        UserEntity user = new UserEntity(1L, "Shane", "Reilly", "123-45-6789", "4 Street Road", "email@gmail.com", "867-5309", "CodingRulez", "secretPassword");

        when(userService.deleteUserById(1L)).thenThrow(new NoSuchElementException("no such user"));

        mockMvc.perform(
                delete("/api/users/{user_id}", user.getUserId()))
                .andExpect(status().isNotFound());

    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}