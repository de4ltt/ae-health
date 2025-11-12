package feo.health.catalog_service.service.provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.ClinicMapper;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import feo.health.catalog_service.service.catalog_item_provider.ClinicItemProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicItemProviderTest {

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private ClinicMapper clinicMapper;

    @InjectMocks
    private ClinicItemProvider provider;

    @Test
    void getType() {
        assertEquals("clinic", provider.getType());
    }

    @Test
    void getCatalogItems_success() throws ExecutionException, InterruptedException {
        List<Long> ids = Collections.singletonList(1L);
        List<Clinic> clinics = Collections.singletonList(new Clinic());
        List<Catalog.CatalogItem> items = Collections.singletonList(Catalog.CatalogItem.newBuilder().build());

        when(clinicRepository.findAllById(ids)).thenReturn(clinics);
        when(clinicMapper.toCatalogItem(clinics)).thenReturn(items);

        CompletableFuture<List<Catalog.CatalogItem>> future = provider.getCatalogItems(ids);
        List<Catalog.CatalogItem> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getCatalogItemId_success() throws ExecutionException, InterruptedException {
        String link = "/clinic";
        Clinic clinic = new Clinic();
        clinic.setId(1L);

        when(clinicRepository.findByLink(link)).thenReturn(Optional.of(clinic));

        CompletableFuture<Long> future = provider.getCatalogItemId(link);
        Long result = future.get();

        assertEquals(1L, result);
    }

    @Test
    void getCatalogItemId_notFound() throws ExecutionException, InterruptedException {
        String link = "/clinic";

        when(clinicRepository.findByLink(link)).thenReturn(Optional.empty());

        CompletableFuture<Long> future = provider.getCatalogItemId(link);
        Long result = future.get();

        assertNull(result);
    }
}