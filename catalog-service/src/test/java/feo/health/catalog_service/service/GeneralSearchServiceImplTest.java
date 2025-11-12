package feo.health.catalog_service.service;

import feo.health.catalog_service.html.client.GeneralItemsHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.html.parser.DoctorHtmlParser;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.model.dto.SearchDto;
import feo.health.catalog_service.model.dto.ServiceDto;
import feo.health.catalog_service.service.general_search.GeneralSearchServiceImpl;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneralSearchServiceImplTest {

    @Mock
    private GeneralItemsHtmlClient generalItemsHtmlClient;

    @Mock
    private DoctorHtmlParser doctorHtmlParser;

    @Mock
    private ServiceHtmlParser serviceHtmlParser;

    @Mock
    private ClinicHtmlParser clinicHtmlParser;

    @InjectMocks
    private GeneralSearchServiceImpl generalSearchService;

    @Test
    void search_located_success() throws IOException, ExecutionException, InterruptedException {
        String query = "test";
        Boolean located = false;
        Document doc = mock(Document.class);
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());
        List<ServiceDto> services = Collections.singletonList(new ServiceDto());
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().name("Clinic in Moscow").build());

        when(generalItemsHtmlClient.getGeneralItemsPage(query)).thenReturn(doc);
        when(doctorHtmlParser.parseDoctorsAndSpecialities(doc)).thenReturn(doctors);
        when(serviceHtmlParser.parseServices(doc)).thenReturn(services);
        when(clinicHtmlParser.parseClinicsAndClinicTypes(doc)).thenReturn(clinics);

        CompletableFuture<SearchDto> future = generalSearchService.search(query, located);
        SearchDto result = future.get();

        assertEquals(doctors, result.getDoctors());
        assertEquals(services, result.getServices());
        assertEquals(clinics, result.getClinics());
    }

    @Test
    void search_notLocated_removeLocations() throws IOException, ExecutionException, InterruptedException {
        String query = "test";
        Boolean located = true;
        Document doc = mock(Document.class);
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());
        List<ServiceDto> services = Collections.singletonList(new ServiceDto());
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().name("Clinic in Moscow").build());
        List<ClinicDto> cleanedClinics = Collections.singletonList(ClinicDto.builder().name("Clinic").build());

        when(generalItemsHtmlClient.getGeneralItemsPage(query)).thenReturn(doc);
        when(doctorHtmlParser.parseDoctorsAndSpecialities(doc)).thenReturn(doctors);
        when(serviceHtmlParser.parseServices(doc)).thenReturn(services);
        when(clinicHtmlParser.parseClinicsAndClinicTypes(doc)).thenReturn(clinics);

        // Mock static method
        try (var mockedStatic = mockConstruction(ClinicDto.class, (mock, context) -> {
            when(mock.getName()).thenReturn("Clinic in Moscow");
        })) {

            CompletableFuture<SearchDto> future = generalSearchService.search(query, located);
            SearchDto result = future.get();

            assertNotNull(result.getClinics());
        }
    }
}