package feo.health.catalog_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pharmacies")
@Data
public class Pharmacy {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private String website;

    @Column(nullable = false)
    private String address;

    @Column
    private String openingHours;
}
