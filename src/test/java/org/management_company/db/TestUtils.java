package org.management_company.db;

import org.management_company.db.domain.entities.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TestUtils {

    public static Address createAddress() {
        Address address = new Address();
        address.setStreet("street");
        address.setHouse("house");
        address.setApartment("apartment");
        return address;
    }

    public static Client createClient() {
        Client client = new Client();
        client.setFirstname("firstname");
        client.setLastname("lastname");
        client.setPhone("123456789");
        client.setEmail("test@example.com");
        client.setPassword("12345");
        return client;
    }

    public static Request createRequest(Client client, Address address, Specialization specialization, Staff staff) {
        Request request = new Request();
        request.setName("name");
        request.setDescription("description");
        request.setSpecialization(specialization);
        request.setAddress(address);
        request.setClient(client);
        request.setStaff(staff);
        return request;
    }

    public static Request createHalfRequest() {
        Request request = new Request();
        request.setName("name");
        request.setDescription("description");
        return request;
    }

    public static Specialization createSpecialization() {
        Specialization specialization = new Specialization();
        specialization.setName("admin");
        return specialization;
    }

    public static Staff createStaff(Specialization specialization) {
        Staff staff = new Staff();
        staff.setFirstname("firstname");
        staff.setLastname("lastname");
        staff.setPhone("123456789");
        staff.setEmail("test@example.com");
        staff.setPassword("12345");
        staff.setSpecialization(specialization);
        return staff;
    }

    public static Review createReview() {
        Review review = new Review();
        review.setScore((short) 5);
        return review;
    }
}
