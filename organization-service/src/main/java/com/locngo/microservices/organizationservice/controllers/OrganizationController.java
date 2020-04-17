package com.locngo.microservices.organizationservice.controllers;

import com.locngo.microservices.organizationservice.model.Organization;
import com.locngo.microservices.organizationservice.services.OrganizationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping(value = "/{organizationId}")
    public Organization getOrganization(@PathVariable("organizationId") String organizationId) {
        return this.organizationService.getOrg(organizationId);
    }

    @PostMapping
    public void saveOrganization(@RequestBody Organization organization) {
        this.organizationService.saveOrg(organization);
    }

    @PutMapping
    public void updateOrganization(@RequestBody Organization organization) {
        this.organizationService.updateOrg(organization);
    }

    @DeleteMapping(value = "/{organizationId}")
    public void deleteOrganization(@PathVariable("organizationId") String organizationId) {
        this.organizationService.deleteOrg(organizationId);
    }
}
