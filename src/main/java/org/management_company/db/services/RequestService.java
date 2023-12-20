package org.management_company.db.services;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.management_company.db.domain.entities.*;
import org.management_company.db.domain.enums.Status;
import org.management_company.db.exceptions.AccessDeniedException;
import org.management_company.db.exceptions.AlreadyReviewException;
import org.management_company.db.exceptions.LookIntoFutureException;
import org.management_company.db.repositories.RequestRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepo requestRepo;

    private final ClientService clientService;

    private final AddressService addressService;

    private final SpecializationService specializationService;

    private final StaffService staffService;

    @Transactional
    public void addNewRequest(Request request,
                              Address address,
                              Specialization specialization,
                              String creatorEmail,
                              List<MultipartFile> files
            ) throws ConstraintViolationException, IOException {
        request.setClient(clientService.findClientByEmail(creatorEmail));
        ValidationService.validateEntity(address);
        request.setAddress(addressService.getUniqeAddress(address));
        ValidationService.validateEntity(specialization);
        request.setSpecialization(specializationService.getUniqeSpecialization(specialization));
        if (!files.isEmpty()) {
            request.setPhotos(convertPhotos(files, request));
        }
        ValidationService.validateEntity(request);
        requestRepo.save(request);
    }

    private List<Photo> convertPhotos(List<MultipartFile> files, Request request)
            throws IOException, ConstraintViolationException {
        ArrayList<Photo> photos = new ArrayList<>();
        for (MultipartFile file : files) {
            Photo photo = toPhotoEntity(file);
            photo.setRequest(request);
            ValidationService.validateEntity(photo);
            photos.add(photo);
        }
        return photos;
    }

    private Photo toPhotoEntity(MultipartFile file)
            throws IOException {
        Photo photo = new Photo();
        photo.setName(file.getOriginalFilename());
        photo.setSize(file.getSize());
        photo.setBytes(file.getBytes());
        return photo;
    }

    @Transactional
    public void changeStatusInProgress(Request request, String staffEmail)
            throws ConstraintViolationException, AccessDeniedException {
        Staff staff = staffService.findStaffByEmail(staffEmail);
        if (!request.getSpecialization().equals(staff.getSpecialization())) {
            throw new AccessDeniedException("Нельзя взять работу не своей специальности");
        }
        request.setStaff(staff);
        request.setStatus(Status.IN_PROGRESS);
        request.setInProgressDateTime(LocalDateTime.now());
        ValidationService.validateEntity(request);
        requestRepo.save(request);
    }

    @Transactional
    public void changeStatusDone(Request request, String staffEmail)
            throws AccessDeniedException, LookIntoFutureException {
        if (!request.getStaff().equals(staffService.findStaffByEmail(staffEmail))) {
            throw new AccessDeniedException("Нельзя менять статус на Выполнено у чужой задачи");
        }
        if (request.getInProgressDateTime() == null) {
            throw new LookIntoFutureException("Задача еще не была взята в работу");
        }
        request.setStatus(Status.DONE);
        request.setDoneDateTime(LocalDateTime.now());
        requestRepo.save(request);
    }

    @Transactional
    public void addNewReview(Request request, Review review, String clientEmail)
            throws ConstraintViolationException, AccessDeniedException, AlreadyReviewException, LookIntoFutureException {
        if (!request.getClient().equals(clientService.findClientByEmail(clientEmail))) {
            throw new AccessDeniedException("Нельзя оставить отзыв на чужую заявку");
        }
        if (request.getDoneDateTime() == null) {
            throw new LookIntoFutureException("Работа еще не выполнена");
        }
        if (request.getReview() != null) {
            throw new AlreadyReviewException("На заявку уже оставлен отзыв");
        }
        ValidationService.validateEntity(review);
        request.setReview(review);
        ValidationService.validateEntity(request);
        requestRepo.save(request);
    }

    public Request findRequestById(Integer id) {
        return requestRepo.findById(id).orElse(null);
    }

    public List<Request> findRequestByClientId(Integer id) {
        return requestRepo.findByClientId(id);
    }

    public List<Request> findRequestByStaffId(Integer id) {
        return requestRepo.findByStaffId(id);
    }

    public List<Request> findRequestBySpecializationId(Integer id) {
        return requestRepo.findBySpecializationId(id);
    }

    public List<Request> findRequestAll() {
        return (List<Request>) requestRepo.findAll();
    }
}
