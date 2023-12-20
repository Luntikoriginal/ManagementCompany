package org.management_company.db.services;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Client;
import org.management_company.db.repositories.ClientRepo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepo clientRepo;

    @Test
    public void testAddNewClient() {
        Client newClient = TestUtils.createClient();
        when(clientRepo.save(newClient)).thenReturn(newClient);

        assertDoesNotThrow(() -> clientService.addNewClient(newClient));
    }

    @Test
    public void testFindClientById() {
        Client client = TestUtils.createClient();
        when(clientRepo.findById(client.getId())).thenReturn(Optional.of(client));

        Client foundClient = clientService.findClientById(client.getId());

        assertNotNull(foundClient);
        assertEquals(client, foundClient);
    }

    @Test
    public void testFindClientByEmail() {
        Client client = TestUtils.createClient();
        when(clientRepo.findByEmail(client.getEmail())).thenReturn(client);

        Client foundClient = clientService.findClientByEmail(client.getEmail());

        assertNotNull(foundClient);
        assertEquals(client, foundClient);
    }

    @Test
    public void testFindClientByPhone() {
        Client client = TestUtils.createClient();
        when(clientRepo.findByPhone(client.getPhone())).thenReturn(client);

        Client foundClient = clientService.findClientByPhone(client.getPhone());

        assertNotNull(foundClient);
        assertEquals(client, foundClient);
    }

    @Test
    public void testFindClientAll() {
        when(clientRepo.findAll()).thenReturn(Collections.emptyList());

        List<Client> foundClients = clientService.findClientAll();

        assertNotNull(foundClients);
        assertTrue(foundClients.isEmpty());
    }

    @Test
    public void testAddNewClient_ValidateFail() {
        Client newClient = TestUtils.createClient();
        newClient.setEmail(null);
        when(clientRepo.save(newClient)).thenReturn(newClient);

        assertThrows(ConstraintViolationException.class, () -> {
            clientService.addNewClient(newClient);
        });
    }
}
