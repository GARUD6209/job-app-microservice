package com.garud.jobms.job.clients;


import com.garud.jobms.job.external.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company",
        url="${company-service.url}")
public interface CompanyClient {

    @GetMapping("/v1/companies/{id}")
    Company getCompany(@PathVariable("id") Long id);
}
