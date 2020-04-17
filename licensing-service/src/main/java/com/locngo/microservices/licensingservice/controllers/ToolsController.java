package com.locngo.microservices.licensingservice.controllers;

import com.locngo.microservices.licensingservice.services.DiscoveryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/tools")
public class ToolsController {

    private final DiscoveryService discoveryService;

    public ToolsController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @GetMapping(value = "/eureka/services")
    public List<String> getEurekaServices() {
        return this.discoveryService.getEurekaServices();
    }
}
