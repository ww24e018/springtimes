package at.idling.springtimes.datapoint;

import at.idling.springtimes.category.Category;
import at.idling.springtimes.category.CategoryService;
import at.idling.springtimes.shared.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DatapointService {

    private final DatapointRepository repository;
    private final CategoryService categoryService;

    public List<DatapointResponse> findByCategoryAndRange(UUID categoryId, Instant from, Instant to) {
        Specification<Datapoint> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            if (from != null) predicates.add(cb.greaterThanOrEqualTo(root.get("recordedAt"), from));
            if (to != null) predicates.add(cb.lessThanOrEqualTo(root.get("recordedAt"), to));
            query.orderBy(cb.asc(root.get("recordedAt")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return repository.findAll(spec).stream().map(DatapointResponse::from).toList();
    }

    public DatapointResponse create(UUID categoryId, DatapointRequest request) {
        Category category = categoryService.findById(categoryId);
        Datapoint dp = Datapoint.builder()
                .category(category)
                .recordedAt(request.recordedAt())
                .value(request.value())
                .build();
        return DatapointResponse.from(repository.save(dp));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Datapoint not found: " + id);
        }
        repository.deleteById(id);
    }
}
