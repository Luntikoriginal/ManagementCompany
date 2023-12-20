package org.management_company.db.repositories;

import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RequestRepoTest {

    @Autowired
    private RequestRepo requestRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    @Autowired
    private StaffRepo staffRepo;

    private Request dancingWithTambourine() {
        Address address = TestUtils.createAddress();
        addressRepo.save(address);

        Client client = TestUtils.createClient();
        clientRepo.save(client);

        Specialization specialization = TestUtils.createSpecialization();
        specializationRepo.save(specialization);

        Staff staff = TestUtils.createStaff(specialization);
        staffRepo.save(staff);

        Request request = TestUtils.createRequest(client, address, specialization, staff);

        return requestRepo.save(request);
    }

    @Test
    public void testFindById() {
        Request savedRequest = dancingWithTambourine();

        Request foundRequest = requestRepo.findById(savedRequest.getId()).orElse(null);

        assertNotNull(foundRequest);
    }

    @Test
    public void testFindByClientId() {
        Request savedRequest = dancingWithTambourine();

        List<Request> foundRequest = requestRepo.findByClientId(savedRequest.getClient().getId());

        assertEquals(foundRequest.size(), 1);
    }

    @Test
    public void testFindByStaffId() {
        Request savedRequest = dancingWithTambourine();

        List<Request> foundRequest = requestRepo.findByStaffId(savedRequest.getStaff().getId());

        assertEquals(foundRequest.size(), 1);
    }

    @Test
    public void testFindBySpecializationId() {
        Request savedRequest = dancingWithTambourine();

        List<Request> foundRequest = requestRepo.findBySpecializationId(savedRequest.getSpecialization().getId());

        assertEquals(foundRequest.size(), 1);
    }

    @Test
    public void testFindAll() {
        Request savedRequest1 = dancingWithTambourine();

        List<Request> foundRequest = (List<Request>) requestRepo.findAll();

        assertEquals(foundRequest.size(), 1);
    }

    @Test
    public void testFindById_NotFound() {
        Request foundRequest = requestRepo.findById(1).orElse(null);

        assertNull(foundRequest);
    }

    @Test
    public void testFindByStaffId_NotFound() {
        List<Request> foundRequest = requestRepo.findByStaffId(1);

        assertEquals(foundRequest.size(), 0);
    }

    @Test
    public void testFindBySpecializationId_NotFound() {
        List<Request> foundRequest = requestRepo.findBySpecializationId(1);

        assertEquals(foundRequest.size(), 0);
    }

    @Test
    public void testFindAll_NotFound() {
        List<Request> foundRequest = (List<Request>) requestRepo.findAll();

        assertEquals(foundRequest.size(), 0);
    }
}

