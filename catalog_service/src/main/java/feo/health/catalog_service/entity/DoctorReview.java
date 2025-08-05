package feo.health.catalog_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "doctor_reviews")
@Data
public class DoctorReview {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Doctor doctor;

    @Column
    private Date date;

    @Column(length = 10000)
    private String text;

    @Column
    private Byte rating;

}
