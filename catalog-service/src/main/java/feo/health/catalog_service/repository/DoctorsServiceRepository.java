package feo.health.catalog_service.repository;

import feo.health.catalog_service.model.entity.DoctorsService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorsServiceRepository extends JpaRepository<DoctorsService, Long> {
}
