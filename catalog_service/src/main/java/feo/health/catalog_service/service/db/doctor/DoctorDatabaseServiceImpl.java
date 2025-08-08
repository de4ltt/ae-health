package feo.health.catalog_service.service.db.doctor;

import feo.health.catalog_service.entity.Doctor;
import feo.health.catalog_service.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorDatabaseServiceImpl implements DoctorDatabaseService {

    private DoctorRepository doctorRepository;

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> saveDoctors(List<Doctor> doctors) {
        return doctorRepository.saveAll(doctors);
    }

    @Override
    public boolean isDoctorPresentByUrl(String url) {
        return doctorRepository.existsByUri(url);
    }

    @Override
    public Optional<Doctor> getDoctorByUrl(String url) {
        return doctorRepository.findByUri(url);
    }

}
