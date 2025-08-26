package feo.health.catalog_service.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Date date;

    @Column(length = 10000)
    private String text;

    @Column
    private Float rating;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Clinic clinic;

    @AssertTrue(message = "Review has to be assigned either to doctor or clinic.")
    public boolean isValidTarget() {
        return (doctor != null) ^ (clinic != null);
    }
}

