package org.management_company.db.services;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.management_company.db.domain.entities.Client;
import org.management_company.db.repositories.ClientRepo;
import org.management_company.db.services.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepo clientRepo;

    @Transactional
    public void addNewClient(Client newClient) throws ConstraintViolationException {
        ValidationService.validateEntity(newClient);
        clientRepo.save(newClient);
    }

    public Client findClientById(Integer id) {
        return clientRepo.findById(id).orElse(null);
    }

    public Client findClientByEmail(String email) {
        return clientRepo.findByEmail(email);
    }

    public Client findClientByPhone(String phone) {
        return clientRepo.findByPhone(phone);
    }

    public List<Client> findClientAll() {
        return (List<Client>) clientRepo.findAll();
    }
}
