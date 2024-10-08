package org.example.proxy;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForwardMultipartController {

    @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void forwardMultipart(ProxyExchange<byte[]> proxyExchange) {
        proxyExchange
                .uri("http://localhost:8081/incoming")
                .post();
    }
}
