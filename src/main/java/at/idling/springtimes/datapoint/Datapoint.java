package at.idling.springtimes.datapoint;

import at.idling.springtimes.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "datapoints")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Datapoint {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal value;
}
