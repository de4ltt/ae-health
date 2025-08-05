package feo.health.catalog_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "clinics")
@Data
public class Clinic {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String uri;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private String imageUri;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicReview> clinicReviews;

}
