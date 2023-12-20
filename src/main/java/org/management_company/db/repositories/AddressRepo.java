package org.management_company.db.repositories;

import org.management_company.db.domain.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepo extends CrudRepository<Address, Integer> {

    Address findByStreetAndHouseAndApartment(String street, String house, String apartment);
}
