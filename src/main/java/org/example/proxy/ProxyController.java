package org.example.proxy;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProxyController {

    @RequestMapping(path = "/api/{*tail}")
    @ResponseBody
    public void useExchange(@PathVariable("tail") String tail, ProxyExchange<byte[]> exchange) {
        exchange.forward(tail);
    }

}
