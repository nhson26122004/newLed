package com.nhson.newserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/get-distance")
    public String getDistance() {
        return String.valueOf(webSocketHandler.getDistanceThreshold());
    }

    @PostMapping("/set-distance")
    public void setDistance(@RequestBody Map<String,String> distance) throws IOException {
        float distanceThreshold = Float.valueOf(distance.get("distance"));
        webSocketHandler.setDistanceThreshold(distanceThreshold);
    }
}
