package com.client.api.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void shouldCreateAccount() throws Exception {
        // Given
        AccountRestModel restModel = AccountRestModel.builder()
                .accountId(1L)
                .accountNumber("001-12345")
                .currency(Currency.ARS)
                .balance(5000.0)
                .active(true)
                .build();
        when(accountService.create(any(Account.class))).thenReturn(restModel);

        // When / Then
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"001-12345\",\"currency\":\"ARS\",\"balance\":5000.0,\"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("001-12345"))
                .andExpect(jsonPath("$.balance").value(5000.0));
    }

    @Test
    void shouldGetAccountById() throws Exception {
        // Given
        AccountRestModel restModel = AccountRestModel.builder()
                .accountId(1L)
                .accountNumber("001-12345")
                .currency(Currency.ARS)
                .balance(5000.0)
                .build();
        when(accountService.findById(1L)).thenReturn(restModel);

        // When / Then
        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("001-12345"))
                .andExpect(jsonPath("$.balance").value(5000.0));
    }

    @Test
    void shouldGetAllAccounts() throws Exception {
        // Given
        List<AccountRestModel> accounts = List.of(
                AccountRestModel.builder().accountId(1L).balance(5000.0).build(),
                AccountRestModel.builder().accountId(2L).balance(3000.0).build()
        );
        when(accountService.findAll()).thenReturn(accounts);

        // When / Then
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateAccount() throws Exception {
        // Given
        AccountRestModel restModel = AccountRestModel.builder()
                .accountId(1L)
                .balance(8000.0)
                .build();
        when(accountService.update(eq(1L), any(Account.class))).thenReturn(restModel);

        // When / Then
        mockMvc.perform(put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":8000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(8000.0));
    }

    @Test
    void shouldDeleteAccountById() throws Exception {
        // When / Then
        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteById(1L);
    }
}
