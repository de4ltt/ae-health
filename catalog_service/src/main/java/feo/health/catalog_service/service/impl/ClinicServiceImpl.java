package feo.health.catalog_service.service.impl;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.entity.Clinic;
import feo.health.catalog_service.html.client.ClinicHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.mapper.ClinicMapper;
import feo.health.catalog_service.service.ClinicDatabaseService;
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

    private final ClinicDatabaseService clinicDatabaseService;

    private final ClinicMapper clinicMapper;

    @Override
    public List<ClinicDto> searchClinics(String query, Boolean located) {
        try {

            Document clinicsPage = htmlClient.getClinicsPage(query);
            Document clinicTypesPage = htmlClient.getClinicTypesPage(query);

            List<ClinicDto> result = new ArrayList<>();
            result.addAll(htmlParser.parseClinics(clinicsPage));
            result.addAll(htmlParser.parseClinicTypes(clinicTypesPage));

            result = located ? removeLocationFromNames(result) : result;

            clinicMapper.toEntity(result);

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClinicDto getClinicInfo(String query, Boolean located) {
        try {
            ClinicDto result = new ClinicDto();
            throw new IOException();
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
