package at.idling.springtimes.datapoint;

import at.idling.springtimes.TestcontainersConfiguration;
import at.idling.springtimes.auth.JwtUtil;
import at.idling.springtimes.category.CategoryRepository;
import at.idling.springtimes.category.CategoryRequest;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class DatapointControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtUtil jwtUtil;
    @Autowired JsonMapper jsonMapper;
    @Autowired DatapointRepository datapointRepository;
    @Autowired CategoryRepository categoryRepository;

    private String token;
    private UUID categoryId;

    @BeforeEach
    void setUp() throws Exception {
        datapointRepository.deleteAll();
        categoryRepository.deleteAll();

        token = jwtUtil.generate();

        String response = mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new CategoryRequest("electricity"))))
                .andReturn().getResponse().getContentAsString();

        categoryId = UUID.fromString(jsonMapper.readTree(response).get("id").asText());
    }

    @Test
    void createDatapoint() throws Exception {
        DatapointRequest request = new DatapointRequest(Instant.now(), new BigDecimal("123.45"));

        mockMvc.perform(post("/categories/" + categoryId + "/datapoints")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.value").value(123.45));
    }

    @Test
    void listDatapointsByRange() throws Exception {
        Instant base = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        mockMvc.perform(post("/categories/" + categoryId + "/datapoints")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new DatapointRequest(base, new BigDecimal("100.00")))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/categories/" + categoryId + "/datapoints")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new DatapointRequest(base.plusSeconds(60), new BigDecimal("200.00")))))
                .andExpect(status().isCreated());

        String from = base.minusSeconds(1).toString();
        String to = base.plusSeconds(120).toString();

        mockMvc.perform(get("/categories/" + categoryId + "/datapoints")
                        .header("Authorization", "Bearer " + token)
                        .param("from", from)
                        .param("to", to))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void createDatapoint_unknownCategory_returnsNotFound() throws Exception {
        DatapointRequest request = new DatapointRequest(Instant.now(), new BigDecimal("1.00"));

        mockMvc.perform(post("/categories/" + UUID.randomUUID() + "/datapoints")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
