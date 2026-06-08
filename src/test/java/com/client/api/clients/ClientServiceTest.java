package com.client.api.clients;

import com.client.api.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClient() {
        // Given
        Client client = new Client();
        client.setName("Juan");
        when(clientRepository.save(client)).thenReturn(client);

        // When
        Client result = clientService.create(client);

        // Then
        assertEquals(client, result);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void shouldFindClientById() {
        // Given
        Client client = new Client();
        client.setClientId(1L);
        client.setName("Juan");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // When
        Client result = clientService.findById(1L);

        // Then
        assertEquals(client, result);
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> clientService.findById(1L));
    }

    @Test
    void shouldReturnAllClients() {
        // Given
        List<Client> clients = List.of(new Client(), new Client());
        when(clientRepository.findAll()).thenReturn(clients);

        // When
        List<Client> result = clientService.findAll();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateClient() {
        // Given
        Client existing = new Client();
        existing.setClientId(1L);
        Client updated = new Client();
        updated.setName("Pedro");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.save(updated)).thenReturn(updated);

        // When
        Client result = clientService.update(1L, updated);

        // Then
        assertEquals(updated, result);
        verify(clientRepository, times(1)).save(updated);
    }

    @Test
    void shouldDeleteClientById() {
        // Given
        Client client = new Client();
        client.setClientId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // When
        clientService.deleteById(1L);

        // Then
        verify(clientRepository, times(1)).deleteById(1L);
    }
}
