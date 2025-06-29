package com.telemetrix.appender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppendController {

    @Value("${POD_NAME:default-java-instance}")
    private String podName;

    @Value("${TARGET_URL:}")
    private String targetUrl;

    @Value("${NODE_TYPE:link}")
    private String nodeType;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/append")
    public AppendResponse appendString(@RequestBody Input input) {
        String result = input.getInput() + " : I am Java instance - " + podName + " ; ";
        System.out.println("Test message");
        
        // Check if this is an end node
        if ("end".equalsIgnoreCase(nodeType)) {
            return new AppendResponse("You reached the End Node", result);
        }
        
        // For begin and link nodes, call the target URL
        if (!targetUrl.isEmpty()) {
            try {
                System.out.println(result);
                restTemplate.postForEntity(targetUrl, result, String.class);
            } catch (RestClientException e) {
                // Log the error or handle it as needed
                System.err.println("Error calling target URL: " + e.getMessage());
            }
        }

        return new AppendResponse("Message processed successfully", result);
    }

    public static class Input {
        private String input;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }

    public static class AppendResponse {
        private String message;
        private String result;

        public AppendResponse(String message, String result) {
            this.message = message;
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}