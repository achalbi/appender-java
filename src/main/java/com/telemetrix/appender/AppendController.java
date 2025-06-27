package com.telemetrix.appender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppendController {

    @Value("${POD_NAME:unknown-pod}")
    private String podName;

    @Value("${TARGET_URL:http://appender-02.appender.svc.cluster.local/append}")
    private String targetUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/append")
    public String appendString(@RequestBody Input input) {
        String response = input.getInput() + " : I am Java instance - " + podName;
        System.out.println("Test message");
        if (!targetUrl.isEmpty()) {
            try {
                System.out.println(response);
                restTemplate.postForEntity(targetUrl, response, String.class);
            } catch (Exception e) {
                // Log the error or handle it as needed
                System.err.println("Error calling target URL: " + e.getMessage());
            }
        }

        return response;
    }

    static class Input {
        private String input;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }
}