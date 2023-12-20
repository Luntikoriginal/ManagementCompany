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
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StaffRepoTest {

    @Autowired
    private StaffRepo staffRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    private Staff dancingWithTambourine() {
        Specialization specialization = TestUtils.createSpecialization();
        specializationRepo.save(specialization);

        Staff staff = TestUtils.createStaff(specialization);
        staffRepo.save(staff);

        return staffRepo.save(staff);
    }

    @Test
    public void testFindById() {
        Staff savedStaff = dancingWithTambourine();

        Staff foundStaff = staffRepo.findById(savedStaff.getId()).orElse(null);

        assertNotNull(foundStaff);
    }

    @Test
    public void testFindByEmail() {
        Staff savedStaff = dancingWithTambourine();

        Staff foundStaff = staffRepo.findByEmail(savedStaff.getEmail());

        assertNotNull(foundStaff);
    }

    @Test
    public void testFindByPhone() {
        Staff savedStaff = dancingWithTambourine();

        Staff foundStaff = staffRepo.findByPhone(savedStaff.getPhone());

        assertNotNull(foundStaff);
    }

    @Test
    public void findBySpecializationId() {
        Staff savedStaff = dancingWithTambourine();

        List<Staff> foundStaff = staffRepo.findBySpecializationId(savedStaff.getSpecialization().getId());

        assertEquals(foundStaff.size(), 1);
    }

    @Test
    public void testFindAll() {
        Staff savedStaff = dancingWithTambourine();

        List<Staff> foundStaff = (List<Staff>) staffRepo.findAll();

        assertEquals(foundStaff.size(), 1);
    }

    @Test
    public void testFindById_NotFound() {
        assertNull(staffRepo.findById(1).orElse(null));
    }

    @Test
    public void testFindByEmail_NotFound() {
        assertNull(staffRepo.findByEmail("email"));
    }

    @Test
    public void testFindByPhone_NotFound() {
        assertNull(staffRepo.findByPhone("phone"));
    }
}
