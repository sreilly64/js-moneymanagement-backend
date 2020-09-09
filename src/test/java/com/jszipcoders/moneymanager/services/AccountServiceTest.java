package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.entities.AccountType;
import com.jszipcoders.moneymanager.repositories.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountEntity accountEntity;

    @Before
    public void init(){
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);

        accountEntity = new AccountEntity(2L, AccountType.CHECKING, 1L, 4000.00);

    }

    @Test
    public void findByAccountNumber() {
        when(accountRepository.findById(isA(Long.class))).thenReturn(java.util.Optional.ofNullable(accountEntity));
        AccountEntity actual = accountService.findByAccountNumber(2L);

        Assert.assertEquals(accountEntity, actual);
    }

    @Test
    public void findAllAccountsByUserId() {
    }

    @Test
    public void updateBalance() {
    }

    @Test
    public void createAccount() {
    }

    @Test
    public void deleteAccount() {
    }
}