package org.management_company.db.repositories;

import org.management_company.db.domain.entities.Staff;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StaffRepo extends CrudRepository<Staff, Integer> {

    Staff findByEmail(String email);

    Staff findByPhone(String phone);

    List<Staff> findBySpecializationId(Integer id);
}
