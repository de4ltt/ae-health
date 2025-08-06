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
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "doctor_clinics",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id")
    )
    private List<Doctor> doctors;

    public boolean isFullInfo() {
        return address != null && imageUri != null;
    }

}

