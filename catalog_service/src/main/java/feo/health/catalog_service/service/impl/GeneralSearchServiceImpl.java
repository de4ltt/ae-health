package feo.health.catalog_service.service.impl;

import feo.health.catalog_service.dto.*;
import feo.health.catalog_service.html.client.GeneralItemsHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.html.parser.DoctorHtmlParser;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.service.GeneralSearchService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class GeneralSearchServiceImpl implements GeneralSearchService {

    private final GeneralItemsHtmlClient generalItemsHtmlClient;

    private final DoctorHtmlParser doctorHtmlParser;
    private final ServiceHtmlParser serviceHtmlParser;
    private final ClinicHtmlParser clinicHtmlParser;

    @Override
    public SearchDto search(String query, Boolean located) {
        try {
            Document generalItemsDocument = generalItemsHtmlClient.getGeneralItemsPage(query);
            SearchDto result = new SearchDto();

            result.setDoctors(doctorHtmlParser.parseDoctorsAndSpecialities(generalItemsDocument));
            result.setServices(serviceHtmlParser.parseServices(generalItemsDocument));
            result.setClinics(clinicHtmlParser.parseClinicsAndClinicTypes(generalItemsDocument));

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
