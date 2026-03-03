package at.idling.springtimes.datapoint;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record DatapointResponse(UUID id, UUID categoryId, Instant recordedAt, BigDecimal value) {

    public static DatapointResponse from(Datapoint d) {
        return new DatapointResponse(d.getId(), d.getCategory().getId(), d.getRecordedAt(), d.getValue());
    }
}
