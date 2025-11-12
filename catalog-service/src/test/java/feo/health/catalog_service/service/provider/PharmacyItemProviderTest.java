package feo.health.catalog_service.service.provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.PharmacyMapper;
import feo.health.catalog_service.model.entity.Pharmacy;
import feo.health.catalog_service.repository.PharmacyRepository;
import feo.health.catalog_service.service.catalog_item_provider.PharmacyItemProvider;
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
class PharmacyItemProviderTest {

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private PharmacyMapper pharmacyMapper;

    @InjectMocks
    private PharmacyItemProvider provider;

    @Test
    void getType() {
        assertEquals("pharmacy", provider.getType());
    }

    @Test
    void getCatalogItems_success() throws ExecutionException, InterruptedException {
        List<Long> ids = Collections.singletonList(1L);
        List<Pharmacy> pharmacies = Collections.singletonList(new Pharmacy());
        List<Catalog.CatalogItem> items = Collections.singletonList(Catalog.CatalogItem.newBuilder().build());

        when(pharmacyRepository.findAllById(ids)).thenReturn(pharmacies);
        when(pharmacyMapper.toCatalogItem(pharmacies)).thenReturn(items);

        CompletableFuture<List<Catalog.CatalogItem>> future = provider.getCatalogItems(ids);
        List<Catalog.CatalogItem> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getCatalogItemId_success() throws ExecutionException, InterruptedException {
        String link = "1";
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(1L);

        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));

        CompletableFuture<Long> future = provider.getCatalogItemId(link);
        Long result = future.get();

        assertEquals(1L, result);
    }

    @Test
    void getCatalogItemId_notFound() throws ExecutionException, InterruptedException {
        String link = "1";

        when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

        CompletableFuture<Long> future = provider.getCatalogItemId(link);
        Long result = future.get();

        assertNull(result);
    }
}