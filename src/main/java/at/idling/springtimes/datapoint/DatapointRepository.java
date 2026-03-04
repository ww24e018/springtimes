package at.idling.springtimes.datapoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface DatapointRepository extends JpaRepository<Datapoint, UUID>, JpaSpecificationExecutor<Datapoint> {
}
