package com.locngo.microservices.licensingservice.services;

import com.locngo.microservices.licensingservice.clients.OrganizationDiscoveryClient;
//import com.locngo.microservices.licensingservice.clients.OrganizationFeignClient;
import com.locngo.microservices.licensingservice.clients.OrganizationRestTemplateClient;
import com.locngo.microservices.licensingservice.config.ServiceConfig;
import com.locngo.microservices.licensingservice.model.License;
import com.locngo.microservices.licensingservice.model.Organization;
import com.locngo.microservices.licensingservice.repository.LicenseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class LicenseServiceImpl implements LicenseService{

    private final LicenseRepository licenseRepository;

    private final ServiceConfig config;

    private final OrganizationDiscoveryClient organizationDiscoveryClient;

    private final OrganizationRestTemplateClient organizationRestTemplateClient;

    //private final OrganizationFeignClient organizationFeignClient;

    public LicenseServiceImpl(
            LicenseRepository licenseRepository,
            ServiceConfig config,
            OrganizationDiscoveryClient organizationDiscoveryClient,
            OrganizationRestTemplateClient organizationRestTemplateClient
            //, OrganizationFeignClient organizationFeignClient
    ) {
        this.licenseRepository = licenseRepository;
        this.config = config;
        this.organizationDiscoveryClient = organizationDiscoveryClient;
        this.organizationRestTemplateClient = organizationRestTemplateClient;
        //this.organizationFeignClient = organizationFeignClient;
    }

    @Override
    public License getLicense(String organizationId, String licenseId) {
        License license = this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        return license.witComment(this.config.getExampleProperty());
    }

    @Override
    public License getLicense(String organizationId, String licenseId, String clientType) {
        License license = this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        Organization organization = this.retrieveOrgInfo(organizationId, clientType);
        return license
                .withOrganizationId(organization.getId())
                .withOrganizationName(organization.getName())
                .withContactName(organization.getContactName())
                .withContactEmail(organization.getContactEmail())
                .withContactPhone(organization.getContactPhone())
                .witComment(this.config.getExampleProperty());
    }

    @Override
    public List<License> getLicenseByOrg(String organizationId) {
        return this.licenseRepository.findByOrganizationId(organizationId);
    }

    @Override
    public License saveLicense(License license) {
        license.withId(UUID.randomUUID().toString());
        return this.licenseRepository.save(license);
    }

    private Organization retrieveOrgInfo(String organizationId, String clientType) {

        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                //organization = this.organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest template client");
                organization = this.organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = this.organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = this.organizationRestTemplateClient.getOrganization(organizationId);
        }
        return organization;
    }
}
