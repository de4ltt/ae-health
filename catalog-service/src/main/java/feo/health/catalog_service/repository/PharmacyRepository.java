package feo.health.catalog_service.repository;

import feo.health.catalog_service.model.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    Optional<Pharmacy> findByAddressAndName(String address, String name);
}
