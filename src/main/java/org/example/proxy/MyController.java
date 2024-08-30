package org.example.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);


    private final ObjectMapper objectMapper = new ObjectMapper();


    @RequestMapping(path = "/legacy-route")
    @ResponseBody
    public ResponseEntity<JsonNode> answer(@RequestBody Map<String,String> content) throws JsonProcessingException {
        logger.info(objectMapper.writeValueAsString(content));
        return ResponseEntity.ok(objectMapper.readTree(objectMapper.writeValueAsString(content)));
    }
}
