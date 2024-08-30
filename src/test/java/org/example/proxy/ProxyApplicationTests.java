package org.example.proxy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ProxyApplicationTests {

    private final MyController myController;
    private final MockMvc mockMvc;

    @Autowired
    ProxyApplicationTests(MyController myController, MockMvc mockMvc) {
        this.myController = myController;
        this.mockMvc = mockMvc;
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(myController).isNotNull();
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
    void withProxy() throws Exception {
        mockMvc.perform(post("/api/legacy-route")
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
}
