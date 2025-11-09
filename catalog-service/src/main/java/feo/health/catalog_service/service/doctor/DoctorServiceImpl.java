package feo.health.catalog_service.service.doctor;

import feo.health.catalog_service.html.client.ClinicHtmlClient;
import feo.health.catalog_service.html.client.DoctorHtmlClient;
import feo.health.catalog_service.html.client.ReviewsHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.html.parser.DoctorHtmlParser;
import feo.health.catalog_service.html.parser.ReviewsHtmlParser;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.mapper.DoctorMapper;
import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.model.dto.ReviewDto;
import feo.health.catalog_service.model.entity.Doctor;
import feo.health.catalog_service.repository.DoctorRepository;
import feo.health.catalog_service.service.user.UserService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorHtmlClient doctorHtmlClient;
    private final DoctorHtmlParser doctorHtmlParser;
    private final ClinicHtmlClient clinicHtmlClient;
    private final ReviewsHtmlClient reviewsHtmlClient;
    private final ReviewsHtmlParser reviewsHtmlParser;
    private final ServiceHtmlParser serviceHtmlParser;
    private final ClinicHtmlParser clinicHtmlParser;
    private final UserService userService;

    @Override
    @Async
    public CompletableFuture<List<DoctorDto>> searchDoctors(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<DoctorDto> result = new ArrayList<>();
                Document specialitiesDocument = doctorHtmlClient.getDoctorSpecialitiesPage(query);
                Document doctorsDocument = doctorHtmlClient.getDoctorsPage(query);

                result.addAll(doctorHtmlParser.parseDoctorSpecialities(specialitiesDocument));
                result.addAll(doctorHtmlParser.parseDoctors(doctorsDocument));

                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async
    public CompletableFuture<List<DoctorDto>> getDoctorsBySpeciality(String specialityUri) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doctorsDocument = doctorHtmlClient.getDoctorsBySpecialityPage(specialityUri);
                return doctorHtmlParser.parseDoctors(doctorsDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async
    public CompletableFuture<List<DoctorDto>> getClinicDoctors(String clinicUri) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document clinicDoctorsDocument = clinicHtmlClient.getClinicDoctorsPage(clinicUri);
                return doctorHtmlParser.parseClinicDoctors(clinicDoctorsDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async
    public CompletableFuture<List<ClinicDto>> getDoctorClinics(String uri) {
        return CompletableFuture.supplyAsync(() -> {
            Document doctorDocument = doctorHtmlClient.getDoctorClinicsPage(uri);
            return clinicHtmlParser.parseDoctorClinics(doctorDocument);
        });
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<DoctorDto> getDoctorInfo(String doctorUri, Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Doctor> doctorOptional = doctorRepository.findByLink(doctorUri);

                if (doctorOptional.isPresent()) {
                    Doctor doctor = doctorOptional.get();
                    userService.saveToHistory(doctorMapper.toHistoryRequest(doctor, userId));
                    return doctorMapper.toDto(doctor);
                }

                Document doctorDocument = doctorHtmlClient.getDoctorPage(doctorUri);
                DoctorDto doctorDto = doctorHtmlParser.parseDoctor(doctorDocument);

                try {
                    Document doctorReviewsDocument = reviewsHtmlClient.getDoctorReviewsPage(doctorUri);
                    List<ReviewDto> reviews = reviewsHtmlParser.parseDoctorReviews(doctorReviewsDocument);
                    doctorDto.setReviews(reviews);
                    doctorDto.setRating(DoctorDto.calculateDoctorRating(reviews));
                } catch (Exception ignored) {}

                doctorDto.setServices(serviceHtmlParser.parseDoctorServices(doctorDocument));
                doctorDto.setLink(doctorUri);
                doctorDto.setItemType("doctor");

                Doctor doctor = doctorRepository.save(doctorMapper.toEntity(doctorDto));
                userService.saveToHistory(doctorMapper.toHistoryRequest(doctor, userId));

                return doctorDto;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}