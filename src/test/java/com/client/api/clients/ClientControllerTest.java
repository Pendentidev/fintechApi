package com.client.api.clients;

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
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void shouldCreateClient() throws Exception {
        // Given
        Client client = new Client();
        client.setName("Juan");
        client.setEmail("juan@example.com");
        when(clientService.create(any(Client.class))).thenReturn(client);

        // When / Then
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Juan\",\"email\":\"juan@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void shouldGetClientById() throws Exception {
        // Given
        Client client = new Client();
        client.setClientId(1L);
        client.setName("Juan");
        when(clientService.findById(1L)).thenReturn(client);

        // When / Then
        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldGetAllClients() throws Exception {
        // Given
        when(clientService.findAll()).thenReturn(List.of(new Client(), new Client()));

        // When / Then
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateClient() throws Exception {
        // Given
        Client updated = new Client();
        updated.setClientId(1L);
        updated.setName("Pedro");
        when(clientService.update(eq(1L), any(Client.class))).thenReturn(updated);

        // When / Then
        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Pedro\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
    void shouldDeleteClientById() throws Exception {
        // When / Then
        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteById(1L);
    }
}
