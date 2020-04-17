package com.locngo.microservices.organizationservice.repository;

import com.locngo.microservices.organizationservice.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
}
