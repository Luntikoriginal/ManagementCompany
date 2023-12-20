package org.management_company.db.services;

import lombok.RequiredArgsConstructor;
import org.management_company.db.domain.entities.Specialization;
import org.management_company.db.repositories.SpecializationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationRepo specializationRepo;

    public Specialization getUniqeSpecialization(Specialization specialization) {
        Specialization dbSpecialization = specializationRepo.findByName(specialization.getName());
        if (dbSpecialization == null)
            return specialization;
        return dbSpecialization;
    }

    public List<Specialization> findSpecializationAll() {
        return (List<Specialization>) specializationRepo.findAll();
    }
}
