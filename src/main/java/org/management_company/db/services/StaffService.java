package org.management_company.db.services;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.management_company.db.domain.entities.Specialization;
import org.management_company.db.domain.entities.Staff;
import org.management_company.db.exceptions.AccessDeniedException;
import org.management_company.db.repositories.StaffRepo;
import org.management_company.db.services.SpecializationService;
import org.management_company.db.services.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepo staffRepo;

    private final SpecializationService specializationService;

    @Transactional
    public void addNewStaff(Staff newStaff,
                            Specialization specialization,
                            String creatorEmail) throws ConstraintViolationException, AccessDeniedException {
        if (!findStaffByEmail(creatorEmail).getSpecialization().getName().equals("admin")) {
            throw new AccessDeniedException("Добавлять сотрудников - привилегия администратора");
        }
        newStaff.setSpecialization(specializationService.getUniqeSpecialization(specialization));
        ValidationService.validateEntity(newStaff);
        staffRepo.save(newStaff);
    }

    public Staff findStaffById(Integer id) {
        return staffRepo.findById(id).orElse(null);
    }

    public Staff findStaffByEmail(String email) {
        return staffRepo.findByEmail(email);
    }

    public Staff findStaffByPhone(String phone) {
        return staffRepo.findByPhone(phone);
    }

    public List<Staff> findStaffBySpecializationId(Integer id) {
        return staffRepo.findBySpecializationId(id);
    }

    public List<Staff> findStaffAll() {
        return (List<Staff>) staffRepo.findAll();
    }
}
