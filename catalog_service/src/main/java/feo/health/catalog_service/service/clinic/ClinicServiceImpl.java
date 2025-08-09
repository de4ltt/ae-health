package feo.health.catalog_service.service.clinic;

import feo.health.catalog_service.html.client.ClinicHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.mapper.ClinicMapper;
import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.service.db.clinic.ClinicDatabaseService;
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

            return located ? ClinicDto.removeLocationFromNames(result) : result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClinicDto> getClinicsByType(String uri) {
        try {
            Document clinicsPage = htmlClient.getClinicsByTypePage(uri);
            return htmlParser.parseClinics(clinicsPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClinicDto> getClinicsByService(String uri) {
        try {
            Document clinicsPage = htmlClient.getClinicsByServicesPage(uri);
            return htmlParser.parseClinics(clinicsPage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClinicDto getClinicInfo(String uri, Boolean located) {
        try {
            Optional<Clinic> clinic = clinicDatabaseService.getClinicByLink(uri);

            if (clinic.isPresent())
                return clinicMapper.toDto(clinic.get());

            Document clinicDocument = htmlClient.getClinicPage(uri);
            Document clinicReviewsDocument = htmlClient.getClinicReviewsPage(uri);

            ClinicDto clinicDto = htmlParser.parseClinic(clinicDocument, clinicReviewsDocument);
            clinicDto.setLink(uri);
            clinicDto.setItemType("clinic");

            clinicDatabaseService.saveClinic(clinicMapper.toEntity(clinicDto));

            return clinicDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}