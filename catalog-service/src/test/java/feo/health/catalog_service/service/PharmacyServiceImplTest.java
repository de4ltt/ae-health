package feo.health.catalog_service.service;

import feo.health.catalog_service.mapper.PharmacyMapper;
import feo.health.catalog_service.model.dto.OverpassPharmaciesResponse;
import feo.health.catalog_service.model.dto.PharmacyDto;
import feo.health.catalog_service.model.entity.Pharmacy;
import feo.health.catalog_service.repository.PharmacyRepository;
import feo.health.catalog_service.service.pharmacy.PharmacyServiceImpl;
import feo.health.catalog_service.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PharmacyServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private UserService userService;

    @Mock
    private PharmacyMapper pharmacyMapper;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @InjectMocks
    private PharmacyServiceImpl pharmacyService;

    @Test
    void searchPharmacies_success() throws ExecutionException, InterruptedException {
        Integer radius = 1000;
        Double lat = 55.7558;
        Double lon = 37.6173;

        OverpassPharmaciesResponse response = new OverpassPharmaciesResponse();
        List<PharmacyDto> rawDtos = List.of(
                PharmacyDto.builder().name("Pharmacy A").address("Address A").build(),
                PharmacyDto.builder().name("").address("Address B").build(),
                PharmacyDto.builder().name("Pharmacy C").address("").build()
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OverpassPharmaciesResponse.class)).thenReturn(Mono.just(response));

        try (MockedStatic<PharmacyMapper> mocked = mockStatic(PharmacyMapper.class)) {
            mocked.when(() -> PharmacyMapper.toDtoFromOverpassDto(response)).thenReturn(rawDtos);

            List<PharmacyDto> result = pharmacyService.searchPharmacies(radius, lat, lon).get();

            assertEquals(1, result.size());
            assertEquals("Pharmacy A", result.getFirst().getName());
        }
    }

    @Test
    void visitPharmacy_success() throws ExecutionException, InterruptedException {
        PharmacyDto dto = PharmacyDto.builder().name("Test").build();
        Long userId = 1L;
        Pharmacy entity = new Pharmacy();
        entity.setId(1L);

        when(pharmacyMapper.toEntity(dto)).thenReturn(entity);
        when(pharmacyRepository.save(entity)).thenReturn(entity);
        when(pharmacyMapper.toHistoryRequest(entity, userId)).thenReturn(mock(user.User.SaveToHistoryRequest.class));

        CompletableFuture<Void> future = pharmacyService.visitPharmacy(dto, userId);
        future.get();

        verify(userService).saveToHistory(any());
    }

    @Test
    void getPharmacyById_success() throws ExecutionException, InterruptedException {
        Long id = 1L;
        Pharmacy entity = new Pharmacy();
        PharmacyDto dto = PharmacyDto.builder().name("Test").build();

        when(pharmacyRepository.findById(id)).thenReturn(Optional.of(entity));
        when(pharmacyMapper.toDto(entity)).thenReturn(dto);

        CompletableFuture<PharmacyDto> future = pharmacyService.getPharmacyById(id);
        PharmacyDto result = future.get();

        assertEquals(dto, result);
    }

    @Test
    void getPharmacyById_notFound() {
        Long id = 1L;

        when(pharmacyRepository.findById(id)).thenReturn(Optional.empty());

        CompletableFuture<PharmacyDto> future = pharmacyService.getPharmacyById(id);
        assertThrows(ExecutionException.class, future::get);
    }
}