package org.management_company.db.repositories;

import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClientRepoTest {

    @Autowired
    private ClientRepo clientRepo;

    @Test
    public void testFindById() {
        Client client = TestUtils.createClient();
        Client dbClient = clientRepo.save(client);

        Client foundClient = clientRepo.findById(dbClient.getId()).orElseThrow();

        assertNotNull(foundClient);
    }

    @Test
    public void testFindByEmail() {
        Client client = TestUtils.createClient();
        clientRepo.save(client);

        Client foundClient = clientRepo.findByEmail(client.getEmail());

        assertNotNull(foundClient);
    }

    @Test
    public void testFindByPhone() {
        Client client = TestUtils.createClient();
        clientRepo.save(client);

        Client foundClient = clientRepo.findByPhone(client.getPhone());

        assertNotNull(foundClient);
    }

    @Test
    public void testFindAll() {
        Client client = TestUtils.createClient();
        clientRepo.save(client);

        List<Client> foundClients = (List<Client>) clientRepo.findAll();

        assertEquals(foundClients.size(), 1);
    }

    @Test
    public void testFindById_NotFound() {
        assertNull(clientRepo.findById(1).orElse(null));
    }

    @Test
    public void testFindByEmail_NotFound() {
        assertNull(clientRepo.findByEmail("email"));
    }

    @Test
    public void testFindByPhone_NotFound() {
        assertNull(clientRepo.findByPhone("phone"));
    }
}

