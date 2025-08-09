package feo.health.catalog_service.repository;

import feo.health.catalog_service.model.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    Optional<Speciality> findByNameIgnoreCase(String name);
}
