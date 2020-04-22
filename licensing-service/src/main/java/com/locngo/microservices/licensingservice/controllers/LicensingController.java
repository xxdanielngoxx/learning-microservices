package com.locngo.microservices.licensingservice.controllers;

import com.locngo.microservices.licensingservice.model.License;
import com.locngo.microservices.licensingservice.services.LicenseService;
import com.locngo.microservices.licensingservice.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/organizations/{organizationId}/licenses")
public class LicensingController {

    private static final Logger logger = LoggerFactory.getLogger(LicensingController.class);

    private final LicenseService licenseService;

    public LicensingController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
    public License getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId
    ) {
        return this.licenseService.getLicense(organizationId, licenseId);
    }

    @GetMapping(value = "/{licenseId}/{clientType}")
    public License getLicenseWithClientType(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @PathVariable("clientType") String clientType
    ) {
        return this.licenseService.getLicense(organizationId, licenseId, clientType);
    }

    @GetMapping
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
        logger.debug("LicensingServiceImpl.getLicenseByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return this.licenseService.getLicenseByOrg(organizationId);
    }

    @PostMapping
    public License saveLicense(@PathVariable("organizationId") String organizationId,
                               @RequestBody License license) {
        license.withOrganizationId(organizationId);
        return this.licenseService.saveLicense(license);
    }

    @PutMapping("/{licenseId}")
    public String updateLicense(@PathVariable("licenseId") String licenseId) {
        return String.format("This is put. {id = %s}", licenseId);
    }

    @DeleteMapping("/{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteLicense(@PathVariable("licenseId") String licenseId) {
        return String.format("This is the delete. {id = %s}", licenseId);
    }
}
