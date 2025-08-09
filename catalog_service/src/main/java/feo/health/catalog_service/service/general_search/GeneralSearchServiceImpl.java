package feo.health.catalog_service.service.general_search;

import feo.health.catalog_service.html.client.GeneralItemsHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.html.parser.DoctorHtmlParser;
import feo.health.catalog_service.html.parser.ServiceHtmlParser;
import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.model.dto.SearchDto;
import feo.health.catalog_service.model.dto.ServiceDto;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

            List<DoctorDto> doctorDtos = doctorHtmlParser.parseDoctorsAndSpecialities(generalItemsDocument);
            List<ServiceDto> serviceDtos = serviceHtmlParser.parseServices(generalItemsDocument);
            List<ClinicDto> clinicDtos = clinicHtmlParser.parseClinicsAndClinicTypes(generalItemsDocument);

            clinicDtos = !located ? ClinicDto.removeLocationFromNames(clinicDtos) : clinicDtos;

            result.setDoctors(doctorDtos);
            result.setServices(serviceDtos);
            result.setClinics(clinicDtos);

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
