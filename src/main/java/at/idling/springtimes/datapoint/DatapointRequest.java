package at.idling.springtimes.datapoint;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record DatapointRequest(@NotNull Instant recordedAt, @NotNull BigDecimal value) {
}
