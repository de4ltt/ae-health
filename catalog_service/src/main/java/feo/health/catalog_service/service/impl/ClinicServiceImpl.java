package feo.health.catalog_service.service.impl;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.html.client.ClinicHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.service.ClinicService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicHtmlClient htmlClient;
    private final ClinicHtmlParser htmlParser;

    @Override
    public List<ClinicDto> searchClinics(String query, Boolean located) {
        try {

            Document clinicsPage = htmlClient.getClinicsPage(query);
            Document clinicTypesPage = htmlClient.getClinicTypesPage(query);

            List<ClinicDto> result = new ArrayList<>();
            result.addAll(htmlParser.parseClinics(clinicsPage));
            result.addAll(htmlParser.parseClinicTypes(clinicTypesPage));

            result = located ? removeLocationFromNames(result) : result;

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ClinicDto> removeLocationFromNames(List<ClinicDto> clinicDtos) {
        return clinicDtos.stream().map(this::removeLocationFromName).toList();
    }

    private ClinicDto removeLocationFromName(ClinicDto clinicDto) {
        if (clinicDto.getName().contains(" на "))
            clinicDto.setName(clinicDto.getName().substring(0, clinicDto.getName().indexOf(" на ")).trim());
        return clinicDto;
    }
}
