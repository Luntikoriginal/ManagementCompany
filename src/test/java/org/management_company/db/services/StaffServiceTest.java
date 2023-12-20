package org.management_company.db.services;

import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Specialization;
import org.management_company.db.domain.entities.Staff;
import org.management_company.db.exceptions.AccessDeniedException;
import org.management_company.db.repositories.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StaffServiceTest {

    @Autowired
    private StaffService staffService;

    @MockBean
    private StaffRepo staffRepo;

    @MockBean
    private SpecializationService specializationService;

    @Test
    void testAddNewStaff() {
        Specialization specialization = TestUtils.createSpecialization();
        Staff newStaff = TestUtils.createStaff(specialization);

        when(specializationService.getUniqeSpecialization(specialization)).thenReturn(specialization);
        when(staffRepo.findByEmail(newStaff.getEmail())).thenReturn(newStaff);
        when(staffRepo.save(newStaff)).thenReturn(newStaff);

        assertDoesNotThrow(() -> staffService.addNewStaff(newStaff, specialization, newStaff.getEmail()));
        verify(staffRepo, times(1)).save(newStaff);
    }

    @Test
    public void testFindClientById() {
        Specialization specialization = TestUtils.createSpecialization();
        Staff staff = TestUtils.createStaff(specialization);
        when(staffRepo.findById(staff.getId())).thenReturn(Optional.of(staff));

        Staff foundStaff = staffService.findStaffById(staff.getId());

        assertNotNull(foundStaff);
        assertEquals(staff, foundStaff);
    }

    @Test
    public void testFindClientByEmail() {
        Specialization specialization = TestUtils.createSpecialization();
        Staff staff = TestUtils.createStaff(specialization);
        when(staffRepo.findByEmail(staff.getEmail())).thenReturn(staff);

        Staff foundStaff = staffService.findStaffByEmail(staff.getEmail());

        assertNotNull(foundStaff);
        assertEquals(staff, foundStaff);
    }

    @Test
    public void testFindClientByPhone() {
        Specialization specialization = TestUtils.createSpecialization();
        Staff staff = TestUtils.createStaff(specialization);
        when(staffRepo.findByPhone(staff.getPhone())).thenReturn(staff);

        Staff foundStaff = staffService.findStaffByPhone(staff.getPhone());

        assertNotNull(foundStaff);
        assertEquals(staff, foundStaff);
    }

    @Test
    public void testFindClientAll() {
        when(staffRepo.findAll()).thenReturn(Collections.emptyList());

        List<Staff> foundStaff = staffService.findStaffAll();

        assertNotNull(foundStaff);
        assertTrue(foundStaff.isEmpty());
    }

    @Test
    void testAddNewStaff_AccessDeniedException() {
        Specialization specialization = TestUtils.createSpecialization();
        Staff newStaff = TestUtils.createStaff(specialization);
        Specialization creatorSpec = TestUtils.createSpecialization();
        creatorSpec.setName("noAdmin");
        Staff creator = TestUtils.createStaff(creatorSpec);

        when(specializationService.getUniqeSpecialization(specialization)).thenReturn(specialization);
        when(staffRepo.findByEmail(newStaff.getEmail())).thenReturn(creator);
        when(staffRepo.save(newStaff)).thenReturn(newStaff);

        assertThrows(AccessDeniedException.class, () -> {
            staffService.addNewStaff(newStaff, specialization, creator.getEmail());
        });
    }
}
