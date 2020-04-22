package com.locngo.microservices.licensingservice.services;

import com.locngo.microservices.licensingservice.clients.OrganizationDiscoveryClient;
//import com.locngo.microservices.licensingservice.clients.OrganizationFeignClient;
import com.locngo.microservices.licensingservice.clients.OrganizationRestTemplateClient;
import com.locngo.microservices.licensingservice.config.ServiceConfig;
import com.locngo.microservices.licensingservice.model.License;
import com.locngo.microservices.licensingservice.model.Organization;
import com.locngo.microservices.licensingservice.repository.LicenseRepository;
import com.locngo.microservices.licensingservice.utils.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class LicenseServiceImpl implements LicenseService{

    private static final Logger logger = LoggerFactory.getLogger(LicenseServiceImpl.class);

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

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1000"
                    ),
                    @HystrixProperty(
                            name = "circuitBreaker.requestVolumeThreshold",
                            value = "10"
                    ),
                    @HystrixProperty(
                            name = "circuitBreaker.errorThresholdPercentage",
                            value = "75"
                    ),
                    @HystrixProperty(
                            name = "circuitBreaker.sleepWindowInMilliseconds",
                            value = "7000"
                    ),
                    @HystrixProperty(
                            name = "metrics.rollingStats.timeInMilliseconds",
                            value = "15000"
                    ),
                    @HystrixProperty(
                            name = "metrics.rollingPercentile.numBuckets",
                            value = "5"
                    )
            },
            fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            }
    )
    @Override
    public List<License> getLicenseByOrg(String organizationId) {
        logger.debug("LicensingServiceImpl.getLicenseByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        this.randomlyRunLong();
        return this.licenseRepository.findByOrganizationId(organizationId);
    }

    @Override
    public License saveLicense(License license) {
        license.withId(UUID.randomUUID().toString());
        return this.licenseRepository.save(license);
    }

    @HystrixCommand
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

    private void randomlyRunLong() {
        Random random = new Random();
        int randomNum = random.nextInt(4);
        if (randomNum == 3) sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<License> buildFallbackLicenseList(String organizationId) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId(organizationId)
                .withProductName("Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }
}
