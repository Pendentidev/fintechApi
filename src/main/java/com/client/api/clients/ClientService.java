package com.client.api.clients;

import com.client.api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client update(Long id, Client client) {
        findById(id);
        client.setClientId(id);
        return clientRepository.save(client);
    }

    public void deleteById(Long id) {
        findById(id);
        clientRepository.deleteById(id);
    }
}