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
import java.util.Optional;

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

            List<Clinic> clinics = clinicMapper.toEntity(result);
            clinicDatabaseService.saveClinics(clinics);

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClinicDto> getClinicsByService(String uri) {
        try {
            Document clinicsPage = htmlClient.getClinicsByServicesPage(uri);
            List<ClinicDto> result = htmlParser.parseClinics(clinicsPage);

            List<Clinic> clinics = clinicMapper.toEntity(result);
            clinicDatabaseService.saveClinics(clinics);

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClinicDto getClinicInfo(String uri, Boolean located) {
        try {

            Optional<Clinic> clinic = clinicDatabaseService.getClinicByUri(uri);

            if (clinic.isPresent() && clinic.get().isFullInfo())
                return clinicMapper.toDto(clinic.get());

            Document clinicDocument = htmlClient.getClinicPage(uri);
            Document clinicReviewsDocument = htmlClient.getClinicReviewsPage(uri);

            ClinicDto clinicDto = htmlParser.parseClinic(clinicDocument, clinicReviewsDocument);
            clinicDto.setUri(uri);
            clinicDto.setItemType("clinic");

            return clinicDto;
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
