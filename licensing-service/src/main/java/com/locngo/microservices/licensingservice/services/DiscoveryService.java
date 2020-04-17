package com.locngo.microservices.licensingservice.services;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscoveryService {

    private final DiscoveryClient discoveryClient;

    public DiscoveryService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List getEurekaServices(){
        List<String> services = new ArrayList<>();
        discoveryClient.getServices().forEach(servicesName -> {
            discoveryClient.getInstances(servicesName).forEach(instance -> {
                services.add(String.format("%s, %s", servicesName, instance.getUri().toString()));
            });
        });
        return services;
    }
}
