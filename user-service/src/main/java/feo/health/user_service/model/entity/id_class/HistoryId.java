package feo.health.user_service.model.entity.id_class;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryId implements Serializable {
    protected Long userId;
    protected Long itemId;
    protected String type;
    protected LocalDateTime dateTime;
}