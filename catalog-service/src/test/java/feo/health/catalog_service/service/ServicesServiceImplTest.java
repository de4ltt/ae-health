package feo.health.catalog_service.service;

import feo.health.catalog_service.html.client.ServiceHtmlClient;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.model.dto.ServiceDto;
import feo.health.catalog_service.service.services.ServicesServiceImpl;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicesServiceImplTest {

    @Mock
    private ServiceHtmlClient serviceHtmlClient;

    @Mock
    private ServiceHtmlParser serviceHtmlParser;

    @InjectMocks
    private ServicesServiceImpl servicesService;

    @Test
    void searchServices_success() throws IOException, ExecutionException, InterruptedException {
        String query = "test";
        Document doc = mock(Document.class);
        List<ServiceDto> services = Collections.singletonList(new ServiceDto());

        when(serviceHtmlClient.getServicesPage(query)).thenReturn(doc);
        when(serviceHtmlParser.parseServices(doc)).thenReturn(services);

        CompletableFuture<List<ServiceDto>> future = servicesService.searchServices(query);
        List<ServiceDto> result = future.get();

        assertEquals(1, result.size());
    }
}