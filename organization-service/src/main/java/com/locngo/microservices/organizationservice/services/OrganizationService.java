package com.locngo.microservices.organizationservice.services;

import com.locngo.microservices.organizationservice.model.Organization;
import org.springframework.stereotype.Service;

@Service
public interface OrganizationService {
    Organization getOrg(String organizationId);
    void saveOrg(Organization organization);
    void updateOrg(Organization organization);
    void deleteOrg(String organizationId);
}
