package feo.health.catalog_service.repository;

import feo.health.catalog_service.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByLink(String link);
    Boolean existsByLink(String link);
}
