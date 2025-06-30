package com.telemetrix.appender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import java.util.List;
import java.util.ArrayList;

@RestController
public class AppendController {

    @Value("${POD_NAME:default-java-instance}")
    private String podName;

    @Value("${TARGET_URL:}")
    private String targetUrl;

    @Value("${NODE_TYPE:link}")
    private String nodeType;

    private final RestTemplate restTemplate;

    public AppendController() {
        this.restTemplate = new RestTemplate();
        
        // Create a new list with only JSON converters
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        
        // Set the converters explicitly
        restTemplate.setMessageConverters(converters);
    }

    @PostMapping("/append")
    public AppendResponse appendString(@RequestBody Input input) {
        String result = input.getInput() + " : I am Java instance - " + podName ;
        System.out.println("Debug: message 1");
        
        // Check if this is an end node
        if ("end".equalsIgnoreCase(nodeType)) {
            System.out.println("Debug: message 2" + result + "You reached the End Node");
            return new AppendResponse("You reached the End Node", result);
        }
        System.out.println("Debug: message 3");
        // For begin and link nodes, call the target URL
        if (!targetUrl.isEmpty()) {
            try {
                System.out.println("Sending to target URL: " + targetUrl);
                System.out.println("Data to send: " + result);
                
                // Create headers with JSON content type
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // Create the request body as JSON
                Input targetInput = new Input();
                targetInput.setInput(result);
                
                // Create HTTP entity with headers and body
                HttpEntity<Input> request = new HttpEntity<>(targetInput, headers);
                
                // Send POST request with proper JSON format
                ResponseEntity<AppendResponse> response = restTemplate.postForEntity(targetUrl, request, AppendResponse.class);
                System.out.println("Response status: " + response.getStatusCode());
                System.out.println("Response body: " + response.getBody());
                AppendResponse body = response.getBody();
                if (body != null) {
                    // String combinedMessage = result + " \n " + body.getMessage();
                    String combinedMessage = body.getMessage();
                    return new AppendResponse(combinedMessage, result);
                }
            } catch (RestClientException e) {
                // Log the error or handle it as needed
                System.err.println("Error calling target URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Debug: message 4");
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