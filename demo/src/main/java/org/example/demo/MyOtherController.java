package org.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyOtherController {
    private static final Logger logger = LoggerFactory.getLogger(MyOtherController.class);

    @PostMapping(value = "/incoming", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleIncomingForm(@RequestParam(name = "test") String test) {
        logger.info("handleIncomingForm: {}", test);
        return ResponseEntity.ok("""
                {
                    "value": "%s"
                }
                """.formatted(test));
    }
}
