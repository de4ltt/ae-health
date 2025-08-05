package feo.health.catalog_service.service.impl;

import feo.health.catalog_service.dto.DoctorsServiceDto;
import feo.health.catalog_service.entity.DoctorsService;
import feo.health.catalog_service.repository.DoctorsServiceRepository;
import feo.health.catalog_service.service.DoctorsServiceDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorsServiceDatabaseServiceImpl implements DoctorsServiceDatabaseService {

    private DoctorsServiceRepository doctorServiceRepository;

    @Override
    public DoctorsService saveDoctorService(DoctorsService doctorsService) {
        return doctorServiceRepository.save(doctorsService);
    }

}
