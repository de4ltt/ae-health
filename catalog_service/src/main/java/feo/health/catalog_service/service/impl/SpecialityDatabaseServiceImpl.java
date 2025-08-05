package feo.health.catalog_service.service.impl;

import feo.health.catalog_service.entity.Speciality;
import feo.health.catalog_service.repository.SpecialityRepository;
import feo.health.catalog_service.service.SpecialityDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SpecialityDatabaseServiceImpl implements SpecialityDatabaseService {

    private SpecialityRepository specialityRepository;

    @Override
    public Speciality saveSpeciality(Speciality speciality) {
        return specialityRepository.save(speciality);
    }

    @Override
    public Optional<Speciality> findByNameIgnoreCase(String name) {
        return specialityRepository.findByNameIgnoreCase(name);
    }

}
