package org.management_company.db.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Long size;

    @Lob
    @NotNull
    @Column(columnDefinition = "longblob", nullable = false)
    private byte[] bytes;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
}
