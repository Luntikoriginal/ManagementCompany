package org.management_company.db.repositories;

import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SpecializationRepoTest {

    @Autowired
    private SpecializationRepo specializationRepo;

    @Test
    public void testFindByName() {
        Specialization specialization = TestUtils.createSpecialization();
        specializationRepo.save(specialization);

        Specialization foundSpecialization = specializationRepo.findByName(specialization.getName());

        assertNotNull(foundSpecialization);
    }

    @Test
    public void testFindAll() {
        Specialization specialization1 = TestUtils.createSpecialization();
        specializationRepo.save(specialization1);

        Specialization specialization2 = new Specialization();
        specialization2.setId(10);
        specialization2.setName("spec2");
        specializationRepo.save(specialization2);

        List<Specialization> foundSpecializations = (List<Specialization>) specializationRepo.findAll();

        assertNotNull(foundSpecializations);
    }

    @Test
    public void testFindByStreetAndHouseAndApartment_NotFound() {
        String name = "name";

        Specialization foundSpecialization = specializationRepo.findByName(name);

        assertNull(foundSpecialization);
    }
}