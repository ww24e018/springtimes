package at.idling.springtimes.category;

import at.idling.springtimes.TestcontainersConfiguration;
import at.idling.springtimes.auth.JwtUtil;
import at.idling.springtimes.datapoint.DatapointRepository;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class CategoryControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired JwtUtil jwtUtil;
    @Autowired JsonMapper jsonMapper;
    @Autowired CategoryRepository categoryRepository;
    @Autowired DatapointRepository datapointRepository;

    @BeforeEach
    void setUp() {
        datapointRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void createCategory_requiresAuth() throws Exception {
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new CategoryRequest("electricity"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createAndListCategory() throws Exception {
        String token = jwtUtil.generate();

        mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new CategoryRequest("electricity"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("electricity"))
                .andExpect(jsonPath("$.id").isNotEmpty());

        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("electricity"));
    }

    @Test
    void deleteCategory() throws Exception {
        String token = jwtUtil.generate();

        String response = mockMvc.perform(post("/categories")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(new CategoryRequest("to-delete"))))
                .andReturn().getResponse().getContentAsString();

        String id = jsonMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/categories/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
