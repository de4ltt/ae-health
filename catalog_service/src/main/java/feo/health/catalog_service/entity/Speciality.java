package feo.health.catalog_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "specialities")
@Data
public class Speciality {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "specialities")
    private List<Doctor> doctors;

    @Column
    private String uri;

}

