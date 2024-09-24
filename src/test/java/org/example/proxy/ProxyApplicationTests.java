package org.example.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration
@AutoConfigureMockMvc
class ProxyApplicationTests {

    private final MyController myController;
    private final MockMvc mockMvc;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ProxyApplicationTests(MyController myController, MockMvc mockMvc) {
        this.myController = myController;
        this.mockMvc = mockMvc;
    }

    @Test
    void contextLoads() {
        assertThat(myController).isNotNull();
    }

    @Test
    void noProxy() throws Exception {
        mockMvc.perform(post("/legacy-route")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id" : "unique",
                                    "umlaut": "ä"
                                }
                                """))
                .andExpect(jsonPath("$.id").value("unique"))
                .andExpect(jsonPath("$.umlaut").value("ä"));
    }

    @Test
    void withProxy() {
        var body = """
                {
                    "id" : "unique",
                    "umlaut": "ä"
                }""";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/api/legacy-route",
                new HttpEntity<>(body, headers), HashMap.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .hasEntrySatisfying("id", o -> assertThat(o).isEqualTo("unique"))
                .hasEntrySatisfying("umlaut", o -> assertThat(o).isEqualTo("ä"))
        ;
    }
    @Test
    void noProxy_restTemplate() {
        var body = """
                {
                    "id" : "unique",
                    "umlaut": "ä"
                }""";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var response = restTemplate.postForEntity("http://localhost:" + port + "/legacy-route",
                new HttpEntity<>(body, headers), HashMap.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .hasEntrySatisfying("id", o -> assertThat(o).isEqualTo("unique"))
                .hasEntrySatisfying("umlaut", o -> assertThat(o).isEqualTo("ä"))
        ;
    }
}
