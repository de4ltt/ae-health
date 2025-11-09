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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import user.User;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final WebClient webClient;
    private final UserService userService;
    private final PharmacyMapper pharmacyMapper;
    private final PharmacyRepository pharmacyRepository;

    @Override
    @Async
    public CompletableFuture<List<PharmacyDto>> searchPharmacies(Integer radius, Double lat, Double lon) {

        String query = String.format(
                Locale.US,
                "[out:json];node[amenity=pharmacy](around:%d,%.6f,%.6f);out;",
                radius, lat, lon
        );
        String overpassUrl = "https://overpass-api.de/api/interpreter?data=%s".formatted(query);

        return webClient.get()
                .uri(overpassUrl)
                .retrieve()
                .bodyToMono(OverpassPharmaciesResponse.class)
                .map(PharmacyMapper::toDtoFromOverpassDto)
                .map(this::filterIncorrectPharmacies)
                .toFuture();
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> visitPharmacy(PharmacyDto pharmacyDto, Long userId) {
        return CompletableFuture.runAsync(() -> {
            Pharmacy pharmacy = pharmacyRepository.save(pharmacyMapper.toEntity(pharmacyDto));
            User.SaveToHistoryRequest request = pharmacyMapper.toHistoryRequest(pharmacy, userId);
            userService.saveToHistory(request);
        });
    }

    @Override
    @Async
    public CompletableFuture<PharmacyDto> getPharmacyById(Long id) {
        return CompletableFuture.supplyAsync(() ->
                pharmacyMapper.toDto(pharmacyRepository.findById(id).orElseThrow(EntityExistsException::new))
        );
    }

    private List<PharmacyDto> filterIncorrectPharmacies(List<PharmacyDto> pharmacyDtos) {
        return pharmacyDtos.stream()
                .filter(dto -> dto.getName() != null && !dto.getName().isBlank()
                        && dto.getAddress() != null && !dto.getAddress().isBlank())
                .toList();
    }
}