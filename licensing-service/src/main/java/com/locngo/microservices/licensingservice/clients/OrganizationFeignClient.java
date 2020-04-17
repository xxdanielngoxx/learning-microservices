//package com.locngo.microservices.licensingservice.clients;
//
//import com.locngo.microservices.licensingservice.model.Organization;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.cloud.openfeign.FeignClient;
//
//@FeignClient(value = "organizationservice")
//public interface OrganizationFeignClient {
//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/v1/organizations/{organizationId}",
//            consumes = "application/json"
//    )
//    Organization getOrganization(@PathVariable("organizationId") String organizationId);
//}
