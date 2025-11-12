package feo.health.catalog_service.service.provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.DoctorMapper;
import feo.health.catalog_service.model.entity.Doctor;
import feo.health.catalog_service.repository.DoctorRepository;
import feo.health.catalog_service.service.catalog_item_provider.DoctorItemProvider;
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
class DoctorItemProviderTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorItemProvider provider;

    @Test
    void getType() {
        assertEquals("doctor", provider.getType());
    }

    @Test
    void getCatalogItems_success() throws ExecutionException, InterruptedException {
        List<Long> ids = Collections.singletonList(1L);
        List<Doctor> doctors = Collections.singletonList(new Doctor());
        List<Catalog.CatalogItem> items = Collections.singletonList(Catalog.CatalogItem.newBuilder().build());

        when(doctorRepository.findAllById(ids)).thenReturn(doctors);
        when(doctorMapper.toCatalogItem(doctors)).thenReturn(items);

        CompletableFuture<List<Catalog.CatalogItem>> future = provider.getCatalogItems(ids);
        List<Catalog.CatalogItem> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getCatalogItemId_success() throws ExecutionException, InterruptedException {
        String link = "/doctor";
        Doctor doctor = new Doctor();
        doctor.setId(1L);

        when(doctorRepository.findByLink(link)).thenReturn(Optional.of(doctor));

        CompletableFuture<Long> future = provider.getCatalogItemId(link);
        Long result = future.get();

        assertEquals(1L, result);
    }

    @Test
    void getCatalogItemId_notFound() throws ExecutionException, InterruptedException {
        String link = "/doctor";

        when(doctorRepository.findByLink(link)).thenReturn(Optional.empty());

        CompletableFuture<Long> future = provider.getCatalogItemId(link);
        Long result = future.get();

        assertNull(result);
    }
}