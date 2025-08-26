package feo.health.user_service.model.entity;

import feo.health.user_service.model.entity.id_class.FavouriteId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favourites")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Favourite {

    @EmbeddedId
    private FavouriteId id;

    @Column
    LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;
}
