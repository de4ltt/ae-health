package feo.health.catalog_service.service.pharmacy;

import feo.health.catalog_service.dto.OverpassPharmaciesResponse;
import feo.health.catalog_service.dto.PharmacyDto;
import feo.health.catalog_service.mapper.PharmacyMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final RestTemplate restTemplate;

    private final PharmacyMapper pharmacyMapper;

    public List<PharmacyDto> searchPharmacies(Integer radius, Double lat, Double lon) {

        String query = String.format(
                Locale.US,
                "[out:json];node[amenity=pharmacy](around:%d,%.6f,%.6f);out;",
                radius, lat, lon
        );

        String overpassUrl = "https://overpass-api.de/api/interpreter?data=%s".formatted(query);

        System.out.println(overpassUrl);

        ResponseEntity<OverpassPharmaciesResponse> response = restTemplate
                .getForEntity(overpassUrl, OverpassPharmaciesResponse.class);

        return filterIncorrectPharmacies(pharmacyMapper.toDtoFromOverpassDto(response.getBody()));
    }

    private List<PharmacyDto> filterIncorrectPharmacies(List<PharmacyDto> pharmacyDtos) {
        return pharmacyDtos.stream().filter(pharmacyDto ->
                pharmacyDto.getName() != null && !pharmacyDto.getName().isBlank()
                        && pharmacyDto.getAddress() != null && !pharmacyDto.getAddress().isBlank()
        ).toList();
    }

}
