package org.management_company.db.repositories;

import org.management_company.db.domain.entities.Specialization;
import org.springframework.data.repository.CrudRepository;

public interface SpecializationRepo extends CrudRepository<Specialization, Integer> {

    Specialization findByName(String name);
}
