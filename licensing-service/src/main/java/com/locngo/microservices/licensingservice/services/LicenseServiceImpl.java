package com.locngo.microservices.licensingservice.services;

import com.locngo.microservices.licensingservice.config.ServiceConfig;
import com.locngo.microservices.licensingservice.model.License;
import com.locngo.microservices.licensingservice.repository.LicenseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class LicenseServiceImpl implements LicenseService{

    private final LicenseRepository licenseRepository;

    private final ServiceConfig config;

    public LicenseServiceImpl(LicenseRepository licenseRepository, ServiceConfig config) {
        this.licenseRepository = licenseRepository;
        this.config = config;
    }

    @Override
    public License getLicense(String organizationId, String licenseId) {
        License license = this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        return license.witComment(this.config.getExampleProperty());
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
}
