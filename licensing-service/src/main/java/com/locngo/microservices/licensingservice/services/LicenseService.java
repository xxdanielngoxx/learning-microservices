package com.locngo.microservices.licensingservice.services;

import com.locngo.microservices.licensingservice.model.License;

import java.util.List;

public interface LicenseService {
    public License getLicense(String organizationId, String licenseId);
    public List<License> getLicenseByOrg(String organizationId);
    public License saveLicense(License license);
}
