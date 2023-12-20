package org.management_company.db.services;

import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Address;
import org.management_company.db.repositories.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @MockBean
    private AddressRepo addressRepo;

    @Test
    public void testGetUniqueAddress_NewAddress() {
        Address newAddress = TestUtils.createAddress();
        when(addressRepo.findByStreetAndHouseAndApartment(
                newAddress.getStreet(), newAddress.getHouse(), newAddress.getApartment()))
                .thenReturn(null);

        Address resultAddress = addressService.getUniqeAddress(newAddress);

        assertThat(resultAddress).isEqualTo(newAddress);
    }

    @Test
    public void testGetUniqueAddress_ExistingAddress() {
        Address existingAddress = TestUtils.createAddress();
        when(addressRepo.findByStreetAndHouseAndApartment(
                existingAddress.getStreet(), existingAddress.getHouse(), existingAddress.getApartment()))
                .thenReturn(existingAddress);

        Address newAddress = TestUtils.createAddress();
        Address resultAddress = addressService.getUniqeAddress(newAddress);

        assertThat(resultAddress).isEqualTo(existingAddress);
    }

    @Test
    public void testFindAddressAll() {
        Address address = TestUtils.createAddress();
        when(addressRepo.findAll()).thenReturn(Collections.singletonList(address));

        List<Address> resultAddresses = addressService.findAddressAll();

        assertEquals(resultAddresses.size(), 1);
    }
}

