package org.management_company.db.repositories;

import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AddressRepoTest {

    @Autowired
    private AddressRepo addressRepo;

    @Test
    public void testFindByStreetAndHouseAndApartment() {
        Address address = TestUtils.createAddress();

        addressRepo.save(address);

        Address foundAddress = addressRepo
                .findByStreetAndHouseAndApartment(address.getStreet(), address.getHouse(), address.getApartment());

        assertNotNull(foundAddress);
        assertEquals(foundAddress, address);
    }

    @Test
    public void testFindAll() {
        Address address1 = TestUtils.createAddress();
        addressRepo.save(address1);

        Address address2 = TestUtils.createAddress();
        addressRepo.save(address2);

        List<Address> foundAddresses = (List<Address>) addressRepo.findAll();

        assertNotNull(foundAddresses);
        assertEquals(foundAddresses.size(), 2);
    }

    @Test
    public void testFindByStreetAndHouseAndApartment_NotFound() {
        String street = "street";
        String house = "house";
        String apartment = "apartment";

        Address foundAddress = addressRepo.findByStreetAndHouseAndApartment(street, house, apartment);

        assertNull(foundAddress);
    }
}

