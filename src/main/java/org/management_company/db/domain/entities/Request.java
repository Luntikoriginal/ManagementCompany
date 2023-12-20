package org.management_company.db.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.management_company.db.domain.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Enumerated
    private Status status = Status.NEW;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDateTime = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inProgressDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime doneDateTime;

    @OneToOne
    @JoinColumn(name = "rewiew_id")
    private Review review;

    @OneToMany(mappedBy = "request")
    private List<Photo> photos = new ArrayList<>();
}
