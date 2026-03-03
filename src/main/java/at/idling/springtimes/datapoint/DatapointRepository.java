package at.idling.springtimes.datapoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DatapointRepository extends JpaRepository<Datapoint, UUID> {

    @Query("SELECT d FROM Datapoint d WHERE d.category.id = :categoryId AND d.recordedAt BETWEEN :from AND :to ORDER BY d.recordedAt")
    List<Datapoint> findByCategoryAndRange(
            @Param("categoryId") UUID categoryId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );
}
