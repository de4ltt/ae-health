package feo.health.user_service.repository;

import feo.health.user_service.model.entity.History;
import feo.health.user_service.model.entity.id_class.HistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, HistoryId> {
    List<History> findByUserId(Long userId);
}