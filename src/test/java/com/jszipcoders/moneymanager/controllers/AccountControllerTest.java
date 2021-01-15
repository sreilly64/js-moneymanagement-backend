package com.jszipcoders.moneymanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jszipcoders.moneymanager.dto.AccountDTO;
import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.entities.AccountType;
import com.jszipcoders.moneymanager.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .build();
    }

    @Test
    public void findByAccountNumber() throws Exception {
        AccountEntity accountEntity = new AccountEntity(200L, AccountType.CHECKING, 1L, 4000.00, null);

        when(accountService.findByAccountNumber(accountEntity.getAccountNumber())).thenReturn(accountEntity);
        when(accountService.findByAccountNumber(201L)).thenThrow(new EntityNotFoundException("Account number not found."));

        System.out.println("Account retrieved from service: " + asJsonString(accountService.findByAccountNumber(200L)));
        mockMvc.perform(get("/api/accounts/{accountNumber}", 201L)).andDo(print());

        mockMvc.perform(get("/api/accounts/{accountNumber}", 201L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.accountNumber", is(200)))
                .andExpect(jsonPath("$.type", is("CHECKING")))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.balance", is(4000.00)))
                .andExpect(jsonPath("$.routingNumber", is(394058927)));

//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/{accountNumber}", 200);
//
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        System.out.println("result: " + result.getResponse().getContentAsString());

        verify(accountService, times(1)).findByAccountNumber(200L);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void findByAccountNumberFail() throws Exception {
        when(accountService.findByAccountNumber(230L)).thenThrow(new EntityNotFoundException("no Value present"));

        mockMvc.perform(get("/api/accounts/{account_number}", 230))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).findByAccountNumber(230L);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void findAllAccountsByUserId() throws Exception {
        List<AccountEntity> accountEntityList = Arrays.asList(new AccountEntity(3L, AccountType.CHECKING, 2L, 5000.00, null), new AccountEntity(4L, AccountType.SAVINGS, 2L, 9000.00, null));

        when(accountService.findAllAccountsByUserId(2L)).thenReturn(accountEntityList);

        mockMvc.perform(get("/api/accounts/user/{user_id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountNumber", is(3)))
                .andExpect(jsonPath("$[0].type", is("CHECKING")))
                .andExpect(jsonPath("$[0].userId", is(2)))
                .andExpect(jsonPath("$[0].balance", is(5000.00)))
                .andExpect(jsonPath("$[0].routingNumber", is(394058927)))
                .andExpect(jsonPath("$[1].accountNumber", is(4)))
                .andExpect(jsonPath("$[1].type", is("SAVINGS")))
                .andExpect(jsonPath("$[1].userId", is(2)))
                .andExpect(jsonPath("$[1].balance", is(9000.00)))
                .andExpect(jsonPath("$[1].routingNumber", is(394058927)));

        verify(accountService, times(1)).findAllAccountsByUserId(2L);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void findAllAccountsByUserIdFail() throws Exception {
        when(accountService.findAllAccountsByUserId(540L)).thenThrow(new NoSuchElementException("no Value present"));

        mockMvc.perform(get("/api/accounts/user/{user_id}", 540))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).findAllAccountsByUserId(540L);
        verifyNoMoreInteractions(accountService);
    }

//    @Test
//    public void updateBalance() throws Exception {
//        Double balance = 5000.00;
//
//        when(accountService.updateBalance(2L, 1000.00)).thenReturn(balance);
//
//        mockMvc.perform(
//                put("/api/accounts/{account_number}/update/{amount}", 2L, 1000.00)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(asJsonString(balance)))
//                        .andExpect(status().isOk());
//
//        verify(accountService, times(1)).updateBalance(2L, 1000.00);
//        verifyNoMoreInteractions(accountService);
//    }

    @Test
    public void createAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO(AccountType.CHECKING, 9L, 4000.00, null);
        AccountEntity accountEntity = new AccountEntity(100L, accountDTO.getType(), accountDTO.getUserId(), accountDTO.getBalance(), accountDTO.getNickname());

        when(accountService.createAccount(accountDTO)).thenReturn(accountEntity);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountDTO)))
                .andExpect(status().isCreated())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNumber", is(100)));

        verify(accountService, times(1)).createAccount(accountDTO);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    public void deleteAccount() throws Exception {
       when(accountService.deleteAccount(2L)).thenReturn(true);

       mockMvc.perform(
               delete("/api/accounts/{account_number}", 2L))
               .andExpect(status().isOk());

       verify(accountService, times(1)).deleteAccount(2L);
       verifyNoMoreInteractions(accountService);
    }

    @Test
    public void deleteAccountFail() throws Exception {
        when(accountService.deleteAccount(10L)).thenThrow(new NoSuchElementException("No Value present"));

        mockMvc.perform(
                delete("/accounts/{account_number}", 10L))
                .andExpect(status().isNotFound());

        verifyNoMoreInteractions(accountService);
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