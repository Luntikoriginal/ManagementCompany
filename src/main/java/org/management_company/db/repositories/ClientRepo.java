package org.management_company.db.repositories;

import org.management_company.db.domain.entities.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepo extends CrudRepository<Client, Integer> {

    Client findByEmail(String email);

    Client findByPhone(String phone);
}
