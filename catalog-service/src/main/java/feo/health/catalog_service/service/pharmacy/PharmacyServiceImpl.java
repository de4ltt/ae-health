package feo.health.catalog_service.service.pharmacy;

import feo.health.catalog_service.mapper.PharmacyMapper;
import feo.health.catalog_service.model.dto.OverpassPharmaciesResponse;
import feo.health.catalog_service.model.dto.PharmacyDto;
import feo.health.catalog_service.model.entity.Pharmacy;
import feo.health.catalog_service.repository.PharmacyRepository;
import feo.health.catalog_service.service.user.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import user.User;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final RestTemplate restTemplate;

    private final UserService userService;

    private final PharmacyMapper pharmacyMapper;
    private final PharmacyRepository pharmacyRepository;

    public List<PharmacyDto> searchPharmacies(Integer radius, Double lat, Double lon) {

        String query = String.format(
                Locale.US,
                "[out:json];node[amenity=pharmacy](around:%d,%.6f,%.6f);out;",
                radius, lat, lon
        );

        String overpassUrl = "https://overpass-api.de/api/interpreter?data=%s".formatted(query);

        ResponseEntity<OverpassPharmaciesResponse> response = restTemplate
                .getForEntity(overpassUrl, OverpassPharmaciesResponse.class);

        return filterIncorrectPharmacies(pharmacyMapper.toDtoFromOverpassDto(response.getBody()));
    }

    @Override
    @Transactional
    public void visitPharmacy(PharmacyDto pharmacyDto, Long userId) {
        Pharmacy pharmacy = pharmacyRepository.save(pharmacyMapper.toEntity(pharmacyDto));
        User.SaveToHistoryRequest request = pharmacyMapper.toHistoryRequest(pharmacy, userId);
        userService.saveToHistory(request);
    }

    @Override
    public PharmacyDto getPharmacyById(Long id) {
        return pharmacyMapper.toDto(pharmacyRepository.findById(id).orElseThrow(EntityExistsException::new));
    }

    private List<PharmacyDto> filterIncorrectPharmacies(List<PharmacyDto> pharmacyDtos) {
        return pharmacyDtos.stream().filter(pharmacyDto ->
                pharmacyDto.getName() != null && !pharmacyDto.getName().isBlank()
                        && pharmacyDto.getAddress() != null && !pharmacyDto.getAddress().isBlank()
        ).toList();
    }
}
