package org.management_company.db.services;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.*;
import org.management_company.db.exceptions.AccessDeniedException;
import org.management_company.db.exceptions.AlreadyReviewException;
import org.management_company.db.exceptions.LookIntoFutureException;
import org.management_company.db.repositories.RequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RequestServiceTest {

    @Autowired
    private RequestService requestService;

    @MockBean
    private RequestRepo requestRepo;

    @MockBean
    private ClientService clientService;

    @MockBean
    private AddressService addressService;

    @MockBean
    private SpecializationService specializationService;

    @MockBean
    private StaffService staffService;

    private Request dancingWithTambourine() {
        Client client = TestUtils.createClient();
        Address address = TestUtils.createAddress();
        Specialization specialization = TestUtils.createSpecialization();
        Staff staff = TestUtils.createStaff(specialization);
        return TestUtils.createRequest(client, address, specialization, staff);
    }

    @Test
    void testAddNewRequest() {
        Client client = TestUtils.createClient();
        Address address = TestUtils.createAddress();
        Specialization specialization = TestUtils.createSpecialization();
        List<MultipartFile> files = new ArrayList<>();
        Request request = TestUtils.createHalfRequest();

        when(clientService.findClientByEmail(anyString())).thenReturn(client);
        when(addressService.getUniqeAddress(any())).thenReturn(address);
        when(specializationService.getUniqeSpecialization(any())).thenReturn(specialization);

        assertDoesNotThrow(() -> requestService.addNewRequest(request, address, specialization, client.getEmail(), files));
        verify(requestRepo, times(1)).save(request);
    }

    @Test
    void testChangeStatusInProgress() {
        Request request = dancingWithTambourine();

        when(staffService.findStaffByEmail(anyString())).thenReturn(request.getStaff());
        when(requestRepo.save(any())).thenReturn(request);

        assertDoesNotThrow(() -> requestService.changeStatusInProgress(request, request.getStaff().getEmail()));
        verify(requestRepo, times(1)).save(request);
    }

    @Test
    void testChangeStatusDone() {
        Request request = dancingWithTambourine();
        request.setInProgressDateTime(LocalDateTime.now());

        when(staffService.findStaffByEmail(anyString())).thenReturn(request.getStaff());
        when(requestRepo.save(any())).thenReturn(request);

        assertDoesNotThrow(() -> requestService.changeStatusDone(request, request.getStaff().getEmail()));
        verify(requestRepo, times(1)).save(request);
    }

    @Test
    void testAddNewReview() {
        Request request = dancingWithTambourine();
        request.setInProgressDateTime(LocalDateTime.now());
        request.setDoneDateTime(LocalDateTime.now());
        Review review = TestUtils.createReview();

        when(clientService.findClientByEmail(anyString())).thenReturn(request.getClient());

        assertDoesNotThrow(() -> requestService.addNewReview(request, review, request.getClient().getEmail()));
        verify(requestRepo, times(1)).save(request);
    }

    @Test
    void testFindRequestById() {
        Request request = dancingWithTambourine();

        when(requestRepo.findById(request.getId())).thenReturn(Optional.of(request));

        Request foundRequest = requestService.findRequestById(request.getId());

        assertNotNull(foundRequest);
        assertEquals(foundRequest, request);
    }

    @Test
    void testFindRequestByClientId() {
        Request request = dancingWithTambourine();

        when(requestRepo.findByClientId(request.getClient().getId())).thenReturn(Collections.singletonList(request));

        List<Request> foundRequests = requestService.findRequestByClientId(request.getClient().getId());

        assertEquals(foundRequests.size(), 1);
    }

    @Test
    void testFindRequestByStaffId() {
        Request request = dancingWithTambourine();

        when(requestRepo.findByStaffId(request.getStaff().getId())).thenReturn(Collections.singletonList(request));

        List<Request> foundRequests = requestService.findRequestByStaffId(request.getStaff().getId());

        assertEquals(foundRequests.size(), 1);
    }

    @Test
    void testFindRequestBySpecializationId() {
        Request request = dancingWithTambourine();

        when(requestRepo.findBySpecializationId(request.getSpecialization().getId())).thenReturn(Collections.singletonList(request));

        List<Request> foundRequests = requestService.findRequestBySpecializationId(request.getSpecialization().getId());

        assertEquals(foundRequests.size(), 1);
    }

    @Test
    void testFindRequestAll() {
        Request request1 = dancingWithTambourine();
        Request request2 = dancingWithTambourine();

        when(requestRepo.findAll()).thenReturn(List.of(request1, request2));

        List<Request> foundRequests = requestService.findRequestAll();

        assertEquals(foundRequests.size(), 2);
    }

    @Test
    void testAddNewRequest_ValidateAddressFail() {
        Client client = TestUtils.createClient();
        Address address = TestUtils.createAddress();
        address.setStreet(null);
        Specialization specialization = TestUtils.createSpecialization();
        List<MultipartFile> files = new ArrayList<>();
        Request request = TestUtils.createHalfRequest();

        when(clientService.findClientByEmail(anyString())).thenReturn(client);
        when(addressService.getUniqeAddress(any())).thenReturn(address);
        when(specializationService.getUniqeSpecialization(any())).thenReturn(specialization);

        assertThrows(ConstraintViolationException.class, () -> {
            requestService.addNewRequest(request, address, specialization, client.getEmail(), files);
        });
    }

    @Test
    void testAddNewRequest_ValidateSpecializationFail() {
        Client client = TestUtils.createClient();
        Address address = TestUtils.createAddress();
        Specialization specialization = TestUtils.createSpecialization();
        specialization.setName(null);
        List<MultipartFile> files = new ArrayList<>();
        Request request = TestUtils.createHalfRequest();

        when(clientService.findClientByEmail(anyString())).thenReturn(client);
        when(addressService.getUniqeAddress(any())).thenReturn(address);
        when(specializationService.getUniqeSpecialization(any())).thenReturn(specialization);

        assertThrows(ConstraintViolationException.class, () -> {
            requestService.addNewRequest(request, address, specialization, client.getEmail(), files);
        });
    }

    @Test
    void testAddNewRequest_ValidateRequestFail() {
        Client client = TestUtils.createClient();
        Address address = TestUtils.createAddress();
        Specialization specialization = TestUtils.createSpecialization();
        List<MultipartFile> files = new ArrayList<>();
        Request request = TestUtils.createHalfRequest();
        request.setName(null);

        when(clientService.findClientByEmail(anyString())).thenReturn(client);
        when(addressService.getUniqeAddress(any())).thenReturn(address);
        when(specializationService.getUniqeSpecialization(any())).thenReturn(specialization);

        assertThrows(ConstraintViolationException.class, () -> {
            requestService.addNewRequest(request, address, specialization, client.getEmail(), files);
        });
    }

    @Test
    void testChangeStatusInProgress_AccessFail() {
        Request request = dancingWithTambourine();
        Specialization newSpecialization = TestUtils.createSpecialization();
        newSpecialization.setName("noAdmin");
        request.setSpecialization(newSpecialization);
        when(staffService.findStaffByEmail(anyString())).thenReturn(request.getStaff());
        when(requestRepo.save(any())).thenReturn(request);

        assertThrows(AccessDeniedException.class, () -> {
            requestService.changeStatusInProgress(request, request.getStaff().getEmail());
        });
    }

    @Test
    void testChangeStatusDone_AccessDeniedException() {
        Request request = dancingWithTambourine();
        Staff staff = TestUtils.createStaff(TestUtils.createSpecialization());
        staff.setEmail("email@mail.ru");
        request.setInProgressDateTime(LocalDateTime.now());

        when(staffService.findStaffByEmail(anyString())).thenReturn(staff);
        when(requestRepo.save(any())).thenReturn(request);

        assertThrows(AccessDeniedException.class, () -> {
            requestService.changeStatusDone(request, staff.getEmail());
        });
    }

    @Test
    void testChangeStatusDone_LookIntoFutureException() {
        Request request = dancingWithTambourine();

        when(staffService.findStaffByEmail(anyString())).thenReturn(request.getStaff());
        when(requestRepo.save(any())).thenReturn(request);

        assertThrows(LookIntoFutureException.class, () -> {
            requestService.changeStatusDone(request, request.getStaff().getEmail());
        });
    }

    @Test
    void testAddNewReview_AccessDeniedException() {
        Request request = dancingWithTambourine();
        request.setInProgressDateTime(LocalDateTime.now());
        request.setDoneDateTime(LocalDateTime.now());
        Review review = TestUtils.createReview();
        Client client = TestUtils.createClient();
        client.setEmail("email@mail.ru");

        when(clientService.findClientByEmail(anyString())).thenReturn(client);

        assertThrows(AccessDeniedException.class, () -> {
            requestService.addNewReview(request, review, client.getEmail());
        });
    }

    @Test
    void testAddNewReview_LookIntoFutureException() {
        Request request = dancingWithTambourine();
        request.setInProgressDateTime(LocalDateTime.now());
        Review review = TestUtils.createReview();

        when(clientService.findClientByEmail(anyString())).thenReturn(request.getClient());

        assertThrows(LookIntoFutureException.class, () -> {
            requestService.addNewReview(request, review, request.getClient().getEmail());
        });
    }

    @Test
    void testAddNewReview_AlreadyReviewException() {
        Request request = dancingWithTambourine();
        request.setInProgressDateTime(LocalDateTime.now());
        request.setDoneDateTime(LocalDateTime.now());
        Review review = TestUtils.createReview();
        request.setReview(review);

        when(clientService.findClientByEmail(anyString())).thenReturn(request.getClient());

        assertThrows(AlreadyReviewException.class, () -> {
            requestService.addNewReview(request, review, request.getClient().getEmail());
        });
    }
}
