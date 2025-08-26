package feo.health.catalog_service.service.clinic;

import feo.health.catalog_service.html.client.ClinicHtmlClient;
import feo.health.catalog_service.html.parser.ClinicHtmlParser;
import feo.health.catalog_service.mapper.ClinicMapper;
import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import feo.health.catalog_service.service.user.UserService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicHtmlClient htmlClient;
    private final ClinicHtmlParser htmlParser;

    private final ClinicRepository clinicRepository;

    private final ClinicMapper clinicMapper;

    private final UserService userService;

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
    @Transactional
    public ClinicDto getClinicInfo(String uri, Boolean located, Long userId) {
        try {
            Optional<Clinic> _clinic = clinicRepository.findByLink(uri);

            if (_clinic.isPresent()) {
                Clinic clinic = _clinic.get();
                userService.saveToHistory(clinicMapper.toHistoryRequest(clinic, userId));
                return clinicMapper.toDto(clinic);
            }

            Document clinicDocument = htmlClient.getClinicPage(uri);
            Document clinicReviewsDocument = htmlClient.getClinicReviewsPage(uri);

            ClinicDto clinicDto = htmlParser.parseClinic(clinicDocument, clinicReviewsDocument);
            clinicDto.setLink(uri);
            clinicDto.setItemType("clinic");

            Clinic clinic = clinicRepository.save(clinicMapper.toEntity(clinicDto));
            userService.saveToHistory(clinicMapper.toHistoryRequest(clinic, userId));

            return clinicDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}