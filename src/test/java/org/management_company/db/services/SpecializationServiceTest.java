package org.management_company.db.services;

import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Specialization;
import org.management_company.db.repositories.SpecializationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SpecializationServiceTest {

    @Autowired
    private SpecializationService specializationService;

    @MockBean
    private SpecializationRepo specializationRepo;

    @Test
    public void testGetUniqueSpecialization_NewSpecialization() {
        Specialization newSpecialization = TestUtils.createSpecialization();
        when(specializationRepo.findByName(newSpecialization.getName())).thenReturn(null);

        Specialization resultSpecialization = specializationService.getUniqeSpecialization(newSpecialization);

        assertThat(resultSpecialization).isEqualTo(newSpecialization);
    }

    @Test
    public void testGetUniqueSpecialization_ExistingSpecialization() {
        Specialization existingSpecialization = TestUtils.createSpecialization();
        when(specializationRepo.findByName(existingSpecialization.getName()))
                .thenReturn(existingSpecialization);

        Specialization newSpecialization = TestUtils.createSpecialization();
        Specialization resultAddress = specializationService.getUniqeSpecialization(newSpecialization);

        assertThat(resultAddress).isEqualTo(existingSpecialization);
    }

    @Test
    public void testFindSpecializationAll() {
        Specialization specialization = TestUtils.createSpecialization();
        when(specializationRepo.findAll()).thenReturn(Collections.singletonList(specialization));

        List<Specialization> resultSpecializations = specializationService.findSpecializationAll();

        assertEquals(resultSpecializations.size(), 1);
    }
}
