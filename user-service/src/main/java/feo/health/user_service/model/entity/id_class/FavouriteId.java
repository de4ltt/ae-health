package feo.health.user_service.model.entity.id_class;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavouriteId implements Serializable {
    protected Long userId;
    protected Long itemId;
    protected String type;
}
