package org.management_company.db.services;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ValidationServiceTest {

    @Test
    void testValidateAddress_Good() {
        Address address = TestUtils.createAddress();

        assertDoesNotThrow(() -> ValidationService.validateEntity(address));
    }

    @Test
    void testValidateAddress_Bad() {
        Address address = TestUtils.createAddress();
        address.setStreet(null);

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationService.validateEntity(address);
        });
    }

    @Test
    void testValidateClient_Good() {
        Client client = TestUtils.createClient();

        assertDoesNotThrow(() -> ValidationService.validateEntity(client));
    }

    @Test
    void testValidateClient_Bad() {
        Client client = TestUtils.createClient();
        client.setEmail(null);

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationService.validateEntity(client);
        });
    }

    @Test
    void testValidateRequest_Good() {
        Address address = TestUtils.createAddress();
        Client client = TestUtils.createClient();
        Specialization specialization = TestUtils.createSpecialization();
        Staff staff = TestUtils.createStaff(specialization);
        Request request = TestUtils.createRequest(client, address, specialization, staff);

        assertDoesNotThrow(() -> ValidationService.validateEntity(request));
    }

    @Test
    void testValidateRequest_Bad() {
        Request request = TestUtils.createHalfRequest();

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationService.validateEntity(request);
        });
    }

    @Test
    void testValidateReview_Good() {
        Review review = TestUtils.createReview();

        assertDoesNotThrow(() -> ValidationService.validateEntity(review));
    }

    @Test
    void testValidateReview_Bad() {
        Review review = TestUtils.createReview();
        review.setScore((short) 7);

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationService.validateEntity(review);
        });
    }

    @Test
    void testValidateSpecialization_Good() {
        Specialization specialization = TestUtils.createSpecialization();

        assertDoesNotThrow(() -> ValidationService.validateEntity(specialization));
    }

    @Test
    void testValidateSpecialization_Bad() {
        Specialization specialization = TestUtils.createSpecialization();
        specialization.setName(null);

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationService.validateEntity(specialization);
        });
    }

    @Test
    void testValidateStaff_Good() {
        Specialization specialization = TestUtils.createSpecialization();
        Staff staff = TestUtils.createStaff(specialization);

        assertDoesNotThrow(() -> ValidationService.validateEntity(staff));
    }

    @Test
    void testValidateStaff_Bad() {
        Specialization specialization = TestUtils.createSpecialization();
        Staff staff = TestUtils.createStaff(specialization);
        staff.setEmail(null);

        assertThrows(ConstraintViolationException.class, () -> {
            ValidationService.validateEntity(staff);
        });
    }
}
