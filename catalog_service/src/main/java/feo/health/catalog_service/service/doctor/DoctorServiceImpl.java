package feo.health.catalog_service.service.doctor;

import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.model.dto.ReviewDto;
import feo.health.catalog_service.html.client.ClinicHtmlClient;
import feo.health.catalog_service.html.client.DoctorHtmlClient;
import feo.health.catalog_service.html.client.ReviewsHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.html.parser.DoctorHtmlParser;
import feo.health.catalog_service.html.parser.ReviewsHtmlParser;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.mapper.DoctorMapper;
import feo.health.catalog_service.service.db.doctor.DoctorDatabaseService;
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

    private final ClinicHtmlClient clinicHtmlClient;

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
        try {
            final Document doctorsDocument = doctorHtmlClient.getDoctorsBySpecialityPage(specialityUri);
            return doctorHtmlParser.parseDoctors(doctorsDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DoctorDto> getClinicDoctors(String clinicUri) {
        try {
            Document clinicDoctorsDocument = clinicHtmlClient.getClinicPage(clinicUri);
            return doctorHtmlParser.parseClinicDoctors(clinicDoctorsDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DoctorDto getDoctorInfo(String doctorUri) {
        try {
            DoctorDto result = doctorMapper.toDto(doctorDatabaseService.getDoctorByUrl(doctorUri).orElse(null));

            if (result != null) return result;

            Document doctorDocument = doctorHtmlClient.getDoctorPage(doctorUri);
            result = doctorHtmlParser.parseDoctor(doctorDocument);

            Document doctorReviewsDocument = reviewsHtmlClient.getDoctorReviewsPage(doctorUri);

            List<ReviewDto> reviews = reviewsHtmlParser.parseDoctorReviews(doctorReviewsDocument);
            result.setReviews(reviews);
            result.setRating(DoctorDto.calculateDoctorRating(reviews));

            result.setServices(serviceHtmlParser.parseDoctorServices(doctorDocument));
            result.setClinics(clinicHtmlParser.parseDoctorClinics(doctorDocument));
            result.setLink(doctorUri);
            result.setItemType("doctor");

            doctorDatabaseService.saveDoctor(doctorMapper.toEntity(result));

            return result;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при парсинге страницы врача", e);
        }
    }
}