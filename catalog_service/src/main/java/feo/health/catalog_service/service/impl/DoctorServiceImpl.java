package feo.health.catalog_service.service.impl;

import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.entity.Doctor;
import feo.health.catalog_service.html.client.DoctorHtmlClient;
import feo.health.catalog_service.html.client.ReviewsHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.html.parser.DoctorHtmlParser;
import feo.health.catalog_service.html.parser.ReviewsHtmlParser;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.mapper.DoctorMapper;
import feo.health.catalog_service.service.DoctorDatabaseService;
import feo.health.catalog_service.service.DoctorService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorDatabaseService doctorDatabaseService;

    private final DoctorMapper doctorMapper;

    private final DoctorHtmlClient doctorHtmlClient;
    private final DoctorHtmlParser doctorHtmlParser;

    private final ReviewsHtmlClient reviewsHtmlClient;
    private final ReviewsHtmlParser reviewsHtmlParser;

    private final ServiceHtmlParser serviceHtmlParser;

    private final ClinicHtmlParser clinicHtmlParser;

    @Override
    public List<DoctorDto> searchDoctors(String query) {
        try {
            List<DoctorDto> result = new ArrayList<>(List.of());

            final Document specialitiesDocument = doctorHtmlClient.getDoctorSpecialitiesPage(query);
            final Document doctorsDocument = doctorHtmlClient.getDoctorsPage(query);

            result.addAll(doctorHtmlParser.parseDoctorSpecialities(specialitiesDocument));
            result.addAll(doctorHtmlParser.parseDoctors(doctorsDocument));

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DoctorDto> getDoctorsBySpeciality(String specialityUri) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public DoctorDto getDoctorInfo(String doctorUri) {
        try {
            DoctorDto result = doctorMapper.toDto(doctorDatabaseService.getDoctorByUrl(doctorUri).orElse(null));

            if (result != null) return result;

            Document doctorDocument = doctorHtmlClient.getDoctorPage(doctorUri);
            result = doctorHtmlParser.parseDoctor(doctorDocument);

            Document doctorReviewsDocument = reviewsHtmlClient.getDoctorReviewsPage(doctorUri);
            result.setReviews(reviewsHtmlParser.parseDoctorReviews(doctorReviewsDocument));

            result.setServices(serviceHtmlParser.parseDoctorServices(doctorDocument));
            result.setClinics(clinicHtmlParser.parseDoctorClinics(doctorDocument));
            result.setUri(doctorUri);
            result.setItemType("doctor");

            saveDoctorToDatabase(result);

            return result;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при парсинге страницы врача", e);
        }
    }

    private void saveDoctorToDatabase(DoctorDto doctorDto) {
        if (doctorDatabaseService.isDoctorPresentByUrl(doctorDto.getUri())) return;
        Doctor doctor = doctorMapper.toEntity(doctorDto);
        doctorDatabaseService.saveDoctor(doctor);
    }

}