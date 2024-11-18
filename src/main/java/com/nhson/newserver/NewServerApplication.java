package com.nhson.newserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
@RestController
public class NewServerApplication {

    private final WebSocketHandler webSocketHandler;

    public NewServerApplication(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewServerApplication.class, args);
    }

    @GetMapping("/distance")
    public String getDistance() {
        return String.valueOf(webSocketHandler.getDistanceThreshold());
    }

    @PostMapping("/distance")
    public void setDistance(Map<String,String> distance) throws IOException {
        webSocketHandler.setDistanceThreshold(Float.parseFloat(distance.get("distance")));
    }
}
