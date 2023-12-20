package org.management_company.db.repositories;

import org.management_company.db.domain.entities.Request;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepo extends CrudRepository<Request, Integer> {

    List<Request> findByClientId(Integer id);

    List<Request> findByStaffId(Integer id);

    List<Request> findBySpecializationId(Integer id);
}
