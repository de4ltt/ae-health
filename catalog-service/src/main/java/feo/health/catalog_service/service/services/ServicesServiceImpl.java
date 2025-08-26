package feo.health.catalog_service.service.services;

import feo.health.catalog_service.html.client.ServiceHtmlClient;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.model.dto.ServiceDto;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ServicesServiceImpl implements ServicesService {

    private final ServiceHtmlClient serviceHtmlClient;
    private final ServiceHtmlParser serviceHtmlParser;

    @Override
    public List<ServiceDto> searchServices(String query) {
        try {
            Document servicesElement = serviceHtmlClient.getServicesPage(query);
            return serviceHtmlParser.parseServices(servicesElement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}