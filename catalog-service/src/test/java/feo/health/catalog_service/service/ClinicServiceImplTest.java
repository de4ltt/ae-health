package feo.health.catalog_service.service;

import feo.health.catalog_service.html.client.ClinicHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.mapper.ClinicMapper;
import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import feo.health.catalog_service.service.clinic.ClinicServiceImpl;
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
class ClinicServiceImplTest {

    @Mock
    private ClinicHtmlClient htmlClient;

    @Mock
    private ClinicHtmlParser htmlParser;

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private ClinicMapper clinicMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void searchClinics_success() throws IOException, ExecutionException, InterruptedException {
        String query = "test";
        Boolean located = true;
        Document clinicsDoc = mock(Document.class);
        Document typesDoc = mock(Document.class);
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().name("Clinic name").build());
        List<ClinicDto> types = Collections.singletonList(ClinicDto.builder().name("Some clinic type").build());

        when(htmlClient.getClinicsPage(query)).thenReturn(clinicsDoc);
        when(htmlClient.getClinicTypesPage(query)).thenReturn(typesDoc);
        when(htmlParser.parseClinics(clinicsDoc)).thenReturn(clinics);
        when(htmlParser.parseClinicTypes(typesDoc)).thenReturn(types);

        CompletableFuture<List<ClinicDto>> future = clinicService.searchClinics(query, located);
        List<ClinicDto> result = future.get();

        assertEquals(2, result.size());
    }

    @Test
    void getClinicsByType_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/type";
        Document doc = mock(Document.class);
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().build());

        when(htmlClient.getClinicsByTypePage(uri)).thenReturn(doc);
        when(htmlParser.parseClinics(doc)).thenReturn(clinics);

        CompletableFuture<List<ClinicDto>> future = clinicService.getClinicsByType(uri);
        List<ClinicDto> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getClinicsByService_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/service";
        Document doc = mock(Document.class);
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().build());

        when(htmlClient.getClinicsByServicesPage(uri)).thenReturn(doc);
        when(htmlParser.parseClinics(doc)).thenReturn(clinics);

        CompletableFuture<List<ClinicDto>> future = clinicService.getClinicsByService(uri);
        List<ClinicDto> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getClinicInfo_fromDb_success() throws ExecutionException, InterruptedException {
        String query = "/clinic";
        Boolean located = true;
        Long userId = 1L;
        Clinic clinic = new Clinic();
        ClinicDto dto = ClinicDto.builder().build();

        when(clinicRepository.findByLink(query)).thenReturn(Optional.of(clinic));
        when(clinicMapper.toDto(clinic)).thenReturn(dto);
        when(clinicMapper.toHistoryRequest(clinic, userId)).thenReturn(mock(User.SaveToHistoryRequest.class));

        CompletableFuture<ClinicDto> future = clinicService.getClinicInfo(query, located, userId);
        ClinicDto result = future.get();

        assertEquals(dto, result);
        verify(userService).saveToHistory(any());
    }

    @Test
    void getClinicInfo_scrape_success() throws IOException, ExecutionException, InterruptedException {
        String query = "/clinic";
        Boolean located = true;
        Long userId = 1L;
        Document clinicDoc = mock(Document.class);
        Document reviewsDoc = mock(Document.class);
        ClinicDto dto = ClinicDto.builder().build();
        Clinic entity = new Clinic();

        when(clinicRepository.findByLink(query)).thenReturn(Optional.empty());
        when(htmlClient.getClinicPage(query)).thenReturn(clinicDoc);
        when(htmlClient.getClinicReviewsPage(query)).thenReturn(reviewsDoc);
        when(htmlParser.parseClinic(clinicDoc, reviewsDoc)).thenReturn(dto);
        when(clinicRepository.save(any())).thenReturn(entity);
        when(clinicMapper.toEntity(any())).thenReturn(entity);
        when(clinicMapper.toHistoryRequest(entity, userId)).thenReturn(mock(User.SaveToHistoryRequest.class));

        CompletableFuture<ClinicDto> future = clinicService.getClinicInfo(query, located, userId);
        ClinicDto result = future.get();

        assertNotNull(result);
        verify(userService).saveToHistory(any());
    }
}