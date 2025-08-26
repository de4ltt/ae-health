package feo.health.user_service.repository;

import feo.health.user_service.model.entity.Favourite;
import feo.health.user_service.model.entity.id_class.FavouriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
    List<Favourite> findByUserId(Long userId);
}
