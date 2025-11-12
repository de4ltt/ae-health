package feo.health.catalog_service.service;

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
import feo.health.catalog_service.service.doctor.DoctorServiceImpl;
import feo.health.catalog_service.service.user.UserService;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private DoctorHtmlClient doctorHtmlClient;

    @Mock
    private DoctorHtmlParser doctorHtmlParser;

    @Mock
    private ClinicHtmlClient clinicHtmlClient;

    @Mock
    private ReviewsHtmlClient reviewsHtmlClient;

    @Mock
    private ReviewsHtmlParser reviewsHtmlParser;

    @Mock
    private ServiceHtmlParser serviceHtmlParser;

    @Mock
    private ClinicHtmlParser clinicHtmlParser;

    @Mock
    private UserService userService;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    void searchDoctors_success() throws IOException, ExecutionException, InterruptedException {
        String query = "test";
        Document specialitiesDoc = mock(Document.class);
        Document doctorsDoc = mock(Document.class);
        List<DoctorDto> specialities = Collections.singletonList(new DoctorDto());
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());

        when(doctorHtmlClient.getDoctorSpecialitiesPage(query)).thenReturn(specialitiesDoc);
        when(doctorHtmlClient.getDoctorsPage(query)).thenReturn(doctorsDoc);
        when(doctorHtmlParser.parseDoctorSpecialities(specialitiesDoc)).thenReturn(specialities);
        when(doctorHtmlParser.parseDoctors(doctorsDoc)).thenReturn(doctors);

        CompletableFuture<List<DoctorDto>> future = doctorService.searchDoctors(query);
        List<DoctorDto> result = future.get();

        assertEquals(2, result.size());
        verify(doctorHtmlClient).getDoctorSpecialitiesPage(query);
        verify(doctorHtmlClient).getDoctorsPage(query);
    }

    @Test
    void searchDoctors_ioException() throws IOException {
        String query = "test";
        when(doctorHtmlClient.getDoctorSpecialitiesPage(query)).thenThrow(IOException.class);

        CompletableFuture<List<DoctorDto>> future = doctorService.searchDoctors(query);
        assertThrows(ExecutionException.class, future::get);
    }

    @Test
    void getDoctorsBySpeciality_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/speciality";
        Document doc = mock(Document.class);
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());

        when(doctorHtmlClient.getDoctorsBySpecialityPage(uri)).thenReturn(doc);
        when(doctorHtmlParser.parseDoctors(doc)).thenReturn(doctors);

        CompletableFuture<List<DoctorDto>> future = doctorService.getDoctorsBySpeciality(uri);
        List<DoctorDto> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getClinicDoctors_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/clinic";
        Document doc = mock(Document.class);
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());

        when(clinicHtmlClient.getClinicDoctorsPage(uri)).thenReturn(doc);
        when(doctorHtmlParser.parseClinicDoctors(doc)).thenReturn(doctors);

        CompletableFuture<List<DoctorDto>> future = doctorService.getClinicDoctors(uri);
        List<DoctorDto> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getDoctorClinics_success() throws ExecutionException, InterruptedException {
        String uri = "/doctor";
        Document doc = mock(Document.class);
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().build());

        when(doctorHtmlClient.getDoctorClinicsPage(uri)).thenReturn(doc);
        when(clinicHtmlParser.parseDoctorClinics(doc)).thenReturn(clinics);

        CompletableFuture<List<ClinicDto>> future = doctorService.getDoctorClinics(uri);
        List<ClinicDto> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getDoctorInfo_fromDb_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/doctor";
        Long userId = 1L;
        Doctor doctor = new Doctor();
        DoctorDto dto = new DoctorDto();

        when(doctorRepository.findByLink(uri)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);
        when(doctorMapper.toHistoryRequest(doctor, userId)).thenReturn(mock(User.SaveToHistoryRequest.class));

        CompletableFuture<DoctorDto> future = doctorService.getDoctorInfo(uri, userId);
        DoctorDto result = future.get();

        assertEquals(dto, result);
        verify(userService).saveToHistory(any());
        verify(doctorHtmlClient, never()).getDoctorPage(any());
    }

    @Test
    void getDoctorInfo_scrape_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/doctor";
        Long userId = 1L;
        Document doc = mock(Document.class);
        Document reviewsDoc = mock(Document.class);
        DoctorDto dto = new DoctorDto();
        List<ReviewDto> reviews = Collections.emptyList();
        Doctor entity = new Doctor();

        when(doctorRepository.findByLink(uri)).thenReturn(Optional.empty());
        when(doctorHtmlClient.getDoctorPage(uri)).thenReturn(doc);
        when(reviewsHtmlClient.getDoctorReviewsPage(uri)).thenReturn(reviewsDoc);
        when(doctorHtmlParser.parseDoctor(doc)).thenReturn(dto);
        when(reviewsHtmlParser.parseDoctorReviews(reviewsDoc)).thenReturn(reviews);
        when(serviceHtmlParser.parseDoctorServices(doc)).thenReturn(Collections.emptyList());
        when(doctorRepository.save(any())).thenReturn(entity);
        when(doctorMapper.toEntity(any())).thenReturn(entity);
        when(doctorMapper.toHistoryRequest(entity, userId)).thenReturn(mock(User.SaveToHistoryRequest.class));

        CompletableFuture<DoctorDto> future = doctorService.getDoctorInfo(uri, userId);
        DoctorDto result = future.get();

        assertNotNull(result);
        verify(userService).saveToHistory(any());
    }
}