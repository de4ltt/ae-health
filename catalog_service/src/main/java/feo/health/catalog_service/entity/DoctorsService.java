package feo.health.catalog_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "doctor_services")
@Data
public class DoctorsService {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Doctor doctor;

    @Column
    private String timeInterval;

    @Column
    private String title;

    @Column
    private BigDecimal price;

}
