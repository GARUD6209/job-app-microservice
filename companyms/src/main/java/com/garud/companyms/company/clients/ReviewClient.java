package com.garud.companyms.company.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "review",
        url="${review-service.url}")
public interface ReviewClient {

    @GetMapping("/v1/reviews/averageRating")
    Double getAverageRatingForCompany(@RequestParam("companyId") Long companyId);

}
