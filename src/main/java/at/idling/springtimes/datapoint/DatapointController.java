package at.idling.springtimes.datapoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories/{categoryId}/datapoints")
@RequiredArgsConstructor
public class DatapointController {

    private final DatapointService service;

    @GetMapping
    public List<DatapointResponse> list(
            @PathVariable UUID categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return service.findByCategoryAndRange(categoryId, from, to);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DatapointResponse create(
            @PathVariable UUID categoryId,
            @Valid @RequestBody DatapointRequest request
    ) {
        return service.create(categoryId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID categoryId, @PathVariable UUID id) {
        service.delete(id);
    }
}
