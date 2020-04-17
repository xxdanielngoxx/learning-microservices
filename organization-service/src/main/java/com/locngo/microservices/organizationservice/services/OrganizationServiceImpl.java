package com.locngo.microservices.organizationservice.services;

import com.locngo.microservices.organizationservice.model.Organization;
import com.locngo.microservices.organizationservice.repository.OrganizationRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
        public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization getOrg(String organizationId) {
        System.out.println("{id: " + organizationId + "}");
        return this.organizationRepository.findById(organizationId).orElse(null);
    }

    @Override
    public void saveOrg(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        this.organizationRepository.save(organization);
    }

    @Override
    public void updateOrg(Organization organization) {
        organizationRepository.save(organization);
    }

    @Override
    public void deleteOrg(String organizationId) {
        this.organizationRepository.deleteById(organizationId);
    }
}
