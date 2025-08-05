package feo.health.catalog_service.repository;

import feo.health.catalog_service.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findByUri(String uri);
    Boolean existsByUri(String uri);
}
