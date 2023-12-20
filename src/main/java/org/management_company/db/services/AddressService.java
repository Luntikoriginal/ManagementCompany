package org.management_company.db.services;

import lombok.RequiredArgsConstructor;
import org.management_company.db.domain.entities.Address;
import org.management_company.db.repositories.AddressRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepo addressRepo;

    public Address getUniqeAddress(Address address) {
        Address dbAddress = addressRepo.findByStreetAndHouseAndApartment(address.getStreet(), address.getHouse(), address.getApartment());
        if (dbAddress == null)
            return address;
        return dbAddress;
    }

    public List<Address> findAddressAll() {
        return (List<Address>) addressRepo.findAll();
    }
}
