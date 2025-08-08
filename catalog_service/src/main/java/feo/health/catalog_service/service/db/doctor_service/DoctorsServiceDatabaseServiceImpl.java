package feo.health.catalog_service.service.db.doctor_service;

import feo.health.catalog_service.entity.DoctorsService;
import feo.health.catalog_service.repository.DoctorsServiceRepository;
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
